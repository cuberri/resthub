package org.resthub.core.context.config;

import com.mysema.commons.lang.Assert;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.parsing.ProblemCollector;
import org.springframework.context.config.AbstractFeatureSpecification;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AspectJTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.RegexPatternTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.StringUtils;

/**
 *
 * @author Loïc Frering <loic.frering@gmail.com>
 */
public abstract class ResthubScanSpec extends AbstractFeatureSpecification {

    public enum Type {
        EXCLUDER, INCLUDER;
    }
    //private static final int EXCLUDER = 0;
    //private static final int EXCLUDER = 1;
    private List<String> basePackages = new ArrayList<String>();
    private Type type = Type.INCLUDER;
    private List<Object> includeFilters = new ArrayList<Object>();
    private List<Object> excludeFilters = new ArrayList<Object>();

    public ResthubScanSpec(Class<? extends AbstractResthubScanExecutor> executorType, String... basePackages) {
        super(executorType);
        Assert.notEmpty(basePackages, "At least one base package must be specified");
        for (String basePackage : basePackages) {
            addBasePackage(basePackage);
        }
    }
    
    ResthubScanSpec addBasePackage(String basePackage) {
        if (StringUtils.hasText(basePackage)) {
            this.basePackages.add(basePackage);
        }
        return this;
    }

    String[] basePackages() {
        return this.basePackages.toArray(new String[this.basePackages.size()]);
    }
    
    public ResthubScanSpec setType(Type type) {
        this.type = type;
        return this;
    }
    
    Type type() {
        return type;
    }

    public ResthubScanSpec includeFilters(TypeFilter... includeFilters) {
        this.includeFilters.clear();
        for (TypeFilter filter : includeFilters) {
            addIncludeFilter(filter);
        }
        return this;
    }

    public ResthubScanSpec addIncludeFilter(TypeFilter includeFilter) {
        Assert.notNull(includeFilter, "includeFilter must not be null");
        this.includeFilters.add(includeFilter);
        return this;
    }

    public ResthubScanSpec addIncludeFilter(String filterType, String expression, ClassLoader classLoader) {
        this.includeFilters.add(new FilterTypeDescriptor(filterType, expression, classLoader));
        return this;
    }

    TypeFilter[] includeFilters() {
        return this.includeFilters.toArray(new TypeFilter[this.includeFilters.size()]);
    }

    public ResthubScanSpec excludeFilters(TypeFilter... excludeFilters) {
        this.excludeFilters.clear();
        for (TypeFilter filter : excludeFilters) {
            addExcludeFilter(filter);
        }
        return this;
    }

    public ResthubScanSpec addExcludeFilter(TypeFilter excludeFilter) {
        Assert.notNull(excludeFilter, "excludeFilter must not be null");
        this.excludeFilters.add(excludeFilter);
        return this;
    }

    public ResthubScanSpec addExcludeFilter(String filterType, String expression, ClassLoader classLoader) {
        this.excludeFilters.add(new FilterTypeDescriptor(filterType, expression, classLoader));
        return this;
    }

    TypeFilter[] excludeFilters() {
        return this.excludeFilters.toArray(new TypeFilter[this.excludeFilters.size()]);
    }

    @Override
    protected void doValidate(ProblemCollector problems) {
        if (this.basePackages.isEmpty()) {
            problems.error("At least one base package must be specified");
        }

        for (int i = 0; i < this.includeFilters.size(); i++) {
            if (this.includeFilters.get(i) instanceof FilterTypeDescriptor) {
                this.includeFilters.set(i, ((FilterTypeDescriptor) this.includeFilters.get(i)).createTypeFilter(problems));
            }
        }

        for (int i = 0; i < this.excludeFilters.size(); i++) {
            if (this.excludeFilters.get(i) instanceof FilterTypeDescriptor) {
                this.excludeFilters.set(i, ((FilterTypeDescriptor) this.excludeFilters.get(i)).createTypeFilter(problems));
            }
        }
    }

    private static class FilterTypeDescriptor {

        private String filterType;
        private String expression;
        private ClassLoader classLoader;

        FilterTypeDescriptor(String filterType, String expression, ClassLoader classLoader) {
            Assert.notNull(filterType, "filterType must not be null");
            Assert.notNull(expression, "expression must not be null");
            Assert.notNull(classLoader, "classLoader must not be null");
            this.filterType = filterType;
            this.expression = expression;
            this.classLoader = classLoader;
        }

        @SuppressWarnings("unchecked")
        TypeFilter createTypeFilter(ProblemCollector problems) {
            try {
                if ("annotation".equalsIgnoreCase(this.filterType)) {
                    return new AnnotationTypeFilter((Class<Annotation>) this.classLoader.loadClass(this.expression));
                } else if ("assignable".equalsIgnoreCase(this.filterType)
                        || "assignable_type".equalsIgnoreCase(this.filterType)) {
                    return new AssignableTypeFilter(this.classLoader.loadClass(this.expression));
                } else if ("aspectj".equalsIgnoreCase(this.filterType)) {
                    return new AspectJTypeFilter(this.expression, this.classLoader);
                } else if ("regex".equalsIgnoreCase(this.filterType)) {
                    return new RegexPatternTypeFilter(Pattern.compile(this.expression));
                } else if ("custom".equalsIgnoreCase(this.filterType)) {
                    Class<?> filterClass = this.classLoader.loadClass(this.expression);
                    if (!TypeFilter.class.isAssignableFrom(filterClass)) {
                        problems.error(String.format("custom type filter class [%s] must be assignable to %s",
                                this.expression, TypeFilter.class));
                    }
                    return (TypeFilter) BeanUtils.instantiateClass(filterClass);
                } else {
                    problems.error(String.format("Unsupported filter type [%s]; supported types are: "
                            + "'annotation', 'assignable[_type]', 'aspectj', 'regex', 'custom'", this.filterType));
                }
            } catch (ClassNotFoundException ex) {
                problems.error("Type filter class not found: " + this.expression, ex);
            } catch (Exception ex) {
                problems.error(ex.getMessage(), ex.getCause());
            }

            return new PlaceholderTypeFilter();
        }

        private class PlaceholderTypeFilter implements TypeFilter {

            public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory)
                    throws IOException {
                throw new UnsupportedOperationException(
                        String.format("match() method for placeholder type filter for "
                        + "{filterType=%s,expression=%s} should never be invoked",
                        filterType, expression));
            }
        }
    }
}

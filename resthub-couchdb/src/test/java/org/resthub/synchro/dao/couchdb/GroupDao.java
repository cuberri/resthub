package org.resthub.synchro.dao.couchdb;

import java.util.Collection;

import org.ektorp.ViewQuery;
import org.resthub.couchdb.dao.CouchDBDao;

public class GroupDao extends CouchDBDao<Group> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Class<Group> getManagedClass() {
		return Group.class;
	} // setClass().
	
	/**
	 * Retrieves id of groups.
	 * 
	 * @param group Group from whom we need id (may be null).
	 * @return the group's id (may be null for new groups and null arguments).
	 */
	@Override
	protected String getId(Group group) {
		return group == null ? null : group.getId();
	} // getId().
	
	/**
	 * TODO
	 */
	@Override
	public Group get(String id) {
		Group group = super.get(id);
		if (group != null) {
			ViewQuery query = new ViewQuery().allDocs().includeDocs(true).keys(
					(Collection<Object>)group.memberIds);
			group.members = connector.queryView(query, User.class);
		}
		return group;
	}
	
	
}
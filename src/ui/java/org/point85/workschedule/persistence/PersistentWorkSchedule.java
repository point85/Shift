/*
MIT License

Copyright (c) 2016 Kent Randall

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package org.point85.workschedule.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.point85.workschedule.Rotation;
import org.point85.workschedule.Team;
import org.point85.workschedule.WorkSchedule;

/**
 * Class to persist a {@link WorkSchedule} to a database
 * @author Kent Randall
 *
 */
public class PersistentWorkSchedule {
	// JPA persistence unit name
	private static final String PERSISTENCE_UNIT = "WORK_SCHEDULE";

	private static final String NQ_WS_BY_KEY = "WS.ByKey";
	private static final String NQ_WS_BY_NAME = "WS.ByName";
	private static final String NQ_WS_NAMES = "WS.Names";
	private static final String NQ_TEAM_BY_KEY = "TEAM.ByKey";
	private static final String NQ_ROTATION_CROSS_REF = "ROTATION.CrossRef";

	// entity manager factory
	private EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);

	// entity manager
	private EntityManager em;
	
	private static PersistentWorkSchedule persistentWorkSchedule = new PersistentWorkSchedule();

	private PersistentWorkSchedule() {
	}
	
	public static PersistentWorkSchedule getInstance() {
		return persistentWorkSchedule;
	}

	// get the EntityManager, create if necessary
	private EntityManager getEntityManager() {

		if (em == null) {
			em = emf.createEntityManager();
		}
		return em;

	}

	// execute the named query
	List<?> executeNamedQuery(String queryName, Map<String, Object> parameters) {
		Query query = getEntityManager().createNamedQuery(queryName);

		if (parameters != null) {
			for (Entry<String, Object> entry : parameters.entrySet()) {
				query.setParameter(entry.getKey(), entry.getValue());
			}
		}
		return query.getResultList();
	}

	public List<String> fetchNames() {
		@SuppressWarnings("unchecked")
		List<String> values = (List<String>) executeNamedQuery(PersistentWorkSchedule.NQ_WS_NAMES, null);

		return values;
	}

	// fetch WorkSchedule by its primary key
	public WorkSchedule fetchWorkScheduleByKey(Integer key) throws Exception {
		WorkSchedule result = null;

		if (key != null) {
			Map<String, Object> parameters = new HashMap<>();
			parameters.put("key", key);

			result = fetchWorkSchedule(NQ_WS_BY_KEY, parameters);
		}
		return result;
	}

	// fetch WorkSchedule by its unique name
	public WorkSchedule fetchWorkScheduleByName(String name) throws Exception {
		WorkSchedule result = null;

		if (name != null) {
			Map<String, Object> parameters = new HashMap<>();
			parameters.put("name", name);

			result = fetchWorkSchedule(NQ_WS_BY_NAME, parameters);
		}
		return result;
	}

	// fetch WorkSchedule by a named query
	WorkSchedule fetchWorkSchedule(String queryName, Map<String, Object> parameters) throws Exception {
		WorkSchedule schedule = null;

		Query query = getEntityManager().createNamedQuery(queryName);

		if (parameters != null) {
			for (Entry<String, Object> entry : parameters.entrySet()) {
				query.setParameter(entry.getKey(), entry.getValue());
			}
		}

		schedule = (WorkSchedule) query.getSingleResult();
		return schedule;
	}

	// remove the WorkSchedule from the persistence context
	public void evictWorkSchedule(WorkSchedule schedule) {
		getEntityManager().detach(schedule);
	}

	// save the WorkSchedule to the database
	public void saveWorkSchedule(WorkSchedule schedule) throws Exception {
		try {
			// start transaction
			getEntityManager().getTransaction().begin();

			// merge this entity into the PU
			getEntityManager().merge(schedule);

			// commit transaction
			getEntityManager().getTransaction().commit();
		} catch (Throwable t) {
			// roll back transaction
			if (getEntityManager().getTransaction().isActive()) {
				getEntityManager().getTransaction().rollback();
			}
			throw new Exception(t.getMessage());
		}
	}

	// delete the WorkSchedule from the database
	public void deleteWorkSchedule(WorkSchedule schedule) throws Exception {
		if (schedule == null) {
			return;
		}

		try {
			// start transaction
			getEntityManager().getTransaction().begin();

			// delete
			getEntityManager().remove(getEntityManager().merge(schedule));

			// commit transaction
			getEntityManager().getTransaction().commit();
		} catch (Throwable t) {
			// roll back transaction
			if (getEntityManager().getTransaction().isActive()) {
				getEntityManager().getTransaction().rollback();
			}
			throw new Exception(t.getMessage());
		}
	}

	// fetch Team by its primary key
	public Team fetchTeamByKey(Integer key) throws Exception {
		Query query = getEntityManager().createNamedQuery(NQ_TEAM_BY_KEY);
		query.setParameter("key", key);

		Team team = (Team) query.getSingleResult();

		return team;
	}

	// get any Team references to the Rotation
	public List<Team> getCrossReferences(Rotation rotation) throws Exception {
		Integer key = rotation.getKey();

		Query query = getEntityManager().createNamedQuery(NQ_ROTATION_CROSS_REF);
		query.setParameter(1, key);

		@SuppressWarnings("unchecked")
		List<Integer> keys = (List<Integer>) query.getResultList();

		List<Team> referencingTeams = new ArrayList<>(keys.size());

		// get the referenced Teams
		for (Integer primaryKey : keys) {
			Team referencing = fetchTeamByKey(primaryKey);
			referencingTeams.add(referencing);
		}
		return referencingTeams;
	}
}

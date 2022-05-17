/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this tgetCurrentSession()plate file, choose Tools | TgetCurrentSession()plates
 * and open the tgetCurrentSession()plate in the editor.
 */
package com.github.lightyagami111.webschedule.design.services;

import ch.lambdaj.Lambda;
import com.github.lightyagami111.webschedule.design.datastructure.CalendarEntity;
import com.github.lightyagami111.webschedule.design.datastructure.EventException;
import com.github.lightyagami111.webschedule.design.datastructure.GroupSortEntity;
import com.github.lightyagami111.webschedule.design.datastructure.LabelEntity;
import com.github.lightyagami111.webschedule.design.datastructure.LinkEntity;
import com.github.lightyagami111.webschedule.design.datastructure.ProjectEntity;
import com.github.lightyagami111.webschedule.design.datastructure.TaskEntity;
import com.github.lightyagami111.webschedule.design.datastructure.TaskRepeatDataEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ivaylo
 */
@Transactional
@Component
public class DaoService {
    
    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DaoService.class.getName());

    @Autowired
    private HibernateTransactionManager hibernateTransactionManager;

    private Session getCurrentSession() {
        return hibernateTransactionManager.getSessionFactory().getCurrentSession();
    }

    private FullTextSession getCurrentFullTextSession() {
        return Search.getFullTextSession(getCurrentSession());
    }

    public int createIndex() {
        getCurrentFullTextSession().purgeAll(TaskEntity.class);
        try {
            getCurrentFullTextSession().createIndexer().startAndWait();
        } catch (InterruptedException ex) {
            log.error(ex.getMessage(), ex);
            return 1;
        }
        return 0;
    }

    public List<TaskEntity> search(String searchTerm) {
        QueryBuilder qb = getCurrentFullTextSession().getSearchFactory().buildQueryBuilder().forEntity(TaskEntity.class).get();
        org.apache.lucene.search.Query luceneQuery = qb
                .keyword().wildcard()
                .onField("text")
                .andField("notes")
                .matching(searchTerm + "*")
                .createQuery();

        FullTextQuery fq = getCurrentFullTextSession().createFullTextQuery(luceneQuery, TaskEntity.class);

        return fq.list();
    }

    public void persist(ProjectEntity p) {
        getCurrentSession().persist(p);
    }

    public List<ProjectEntity> findAllProjects() {
        return getCurrentSession().createQuery("SELECT p FROM ProjectEntity p").list();
    }

    public ProjectEntity findProjectById(Long id) {
        ProjectEntity p = null;
        List list = getCurrentSession().createQuery("SELECT p FROM ProjectEntity p WHERE p.id = :_id").setParameter("_id", id).list();
        if (list != null && !list.isEmpty()) {
            p = (ProjectEntity) list.get(0);
        }
        return p;
    }

    public void updateProject(ProjectEntity p) {
        getCurrentSession().merge(p);
    }

    public void deleteProject(Long id) {
        ProjectEntity p = findProjectById(id);
        List<ProjectEntity> children = getCurrentSession().createQuery("SELECT p FROM ProjectEntity p WHERE p.parent = :_pid").setParameter("_pid", String.valueOf(id)).list();
        if (children != null && !children.isEmpty()) {
            for (ProjectEntity children1 : children) {
                getCurrentSession().delete(children1);
            }
        }
        getCurrentSession().delete(p);

        deleteGroupAndSort("projects", String.valueOf(id));
    }

    public ProjectEntity findDefaultProject() {
        ProjectEntity p = null;
        List list = getCurrentSession().createQuery("SELECT p FROM ProjectEntity p WHERE p.defaultProject = TRUE").list();
        if (list != null && !list.isEmpty()) {
            p = (ProjectEntity) list.get(0);
        }
        return p;
    }

    public void setDefaultProject(Long id) {
        List<ProjectEntity> findAllProjects = findAllProjects();
        for (ProjectEntity findAllProject : findAllProjects) {
            findAllProject.setDefaultProject(Boolean.FALSE);
            updateProject(findAllProject);
        }

        ProjectEntity findProjectById = findProjectById(id);
        findProjectById.setDefaultProject(Boolean.TRUE);
        updateProject(findProjectById);
    }

    //--------------------------------------------------------------------------
    // CALENDARS
    //--------------------------------------------------------------------------
    public void persist(CalendarEntity ce) {
        getCurrentSession().persist(ce);
    }

    public void update(CalendarEntity ce) {
        getCurrentSession().merge(ce);
    }

    public List<CalendarEntity> findAllCalendars() {
        return getCurrentSession().createQuery("SELECT ce FROM CalendarEntity ce").list();
    }

    public List<CalendarEntity> findSelectedCalendars() {
        return getCurrentSession().createQuery("SELECT ce FROM CalendarEntity ce WHERE ce.selected = true").list();
    }

    public CalendarEntity findCalendarById(Long id) {
        return (CalendarEntity) getCurrentSession()
                .createQuery("SELECT ce FROM CalendarEntity ce WHERE ce.id = :_id")
                .setParameter("_id", id)
                .uniqueResult();
    }

    public void deleteCalendar(Long id) {
        CalendarEntity findDefaultCalendar = findDefaultCalendar();
        getCurrentSession()
                .createQuery("UPDATE TaskEntity t SET t.calendar = :_cal WHERE t.calendar.id = :_id")
                .setParameter("_id", id)
                .setParameter("_cal", findDefaultCalendar)
                .executeUpdate();
        CalendarEntity ce = findCalendarById(id);
        getCurrentSession().delete(ce);
    }

    
    public CalendarEntity findDefaultCalendar() {
        CalendarEntity p = null;
        List list = getCurrentSession().createQuery("SELECT p FROM CalendarEntity p WHERE p.defaultCalendar = TRUE").list();
        if (list != null && !list.isEmpty()) {
            p = (CalendarEntity) list.get(0);
        }
        return p;
    }

    public void setDefaultCalendar(Long id) {
        List<CalendarEntity> findAllCalendars = findAllCalendars();
        for (CalendarEntity findAllCalendar : findAllCalendars) {
            findAllCalendar.setDefaultCalendar(Boolean.FALSE);
            update(findAllCalendar);
        }

        CalendarEntity findCalendarById = findCalendarById(id);
        findCalendarById.setDefaultCalendar(Boolean.TRUE);
        findCalendarById.setSelected(Boolean.TRUE);
        update(findCalendarById);
    }


    //--------------------------------------------------------------------------
    // LABELS
    //--------------------------------------------------------------------------
    public void persist(LabelEntity p) {
        getCurrentSession().persist(p);
    }

    public List<LabelEntity> findAllLabels() {
        return getCurrentSession().createQuery("SELECT p FROM LabelEntity p").list();
    }

    public LabelEntity findLabelById(Long id) {
        LabelEntity p = null;
        List list = getCurrentSession().createQuery("SELECT p FROM LabelEntity p WHERE p.id = :_id").setParameter("_id", id).list();
        if (list != null && !list.isEmpty()) {
            p = (LabelEntity) list.get(0);
        }
        return p;
    }

    public void updateLabel(LabelEntity p) {
        getCurrentSession().merge(p);
    }

    public void deleteLabel(Long id) {
        LabelEntity p = findLabelById(id);
        getCurrentSession().delete(p);

        deleteGroupAndSort("labels", String.valueOf(id));
    }

    //--------------------------------------------------------------------------
    // TASKS
    //--------------------------------------------------------------------------
    public Long save(TaskEntity p) {
        return (Long) getCurrentSession().save(p);
    }

    public void saveOrUpdate(TaskEntity t) {
        getCurrentSession().saveOrUpdate(t);
    }

    public void deleteDateData(Long task_id) {
        TaskEntity t = findTaskById(task_id);
        t.setStart(null);
        t.setEnd(null);
        t.setAllDay(Boolean.TRUE);
        saveOrUpdate(t);
    }

    public TaskEntity findTaskById(Long id) {
        TaskEntity p = null;
        List list = getCurrentSession().createQuery("SELECT p FROM TaskEntity p WHERE p.id = :_id").setParameter("_id", id).list();
        if (list != null && !list.isEmpty()) {
            p = (TaskEntity) list.get(0);
        }
        return p;
    }

    public List<TaskEntity> loadTasksByProject(Long project_id) {
        return getCurrentSession().createQuery("SELECT p FROM TaskEntity p WHERE p.project.id = :_id").setParameter("_id", project_id).list();
    }

    public Long countTasksById(Long project_id) {
        return (Long) getCurrentSession().createQuery("SELECT COUNT(*) FROM TaskEntity p WHERE p.project.id = :_id").setParameter("_id", project_id).uniqueResult();
    }

    public List<TaskEntity> loadTaskByLabel(Long label_id) {
        return getCurrentSession().createQuery("SELECT p FROM TaskEntity p JOIN p.labels pl WHERE pl.id = :_id").setParameter("_id", label_id).list();
    }

    public List<TaskEntity> findAllSubtasks(String parent_id) {
        List<TaskEntity> resultList = new ArrayList<>();
        List<TaskEntity> list = getCurrentSession().createQuery("SELECT p FROM TaskEntity p WHERE p.parent = :_id").setParameter("_id", parent_id).list();
        if (!list.isEmpty()) {
            resultList.addAll(list);
            for (TaskEntity taskEntity : list) {
                resultList.addAll(findAllSubtasks(String.valueOf(taskEntity.getId())));
            }
        }
        return resultList;
    }

    public void deleteTask(Long task_id) {
        TaskEntity t = findTaskById(task_id);
        List<TaskEntity> findAllSubtasks = findAllSubtasks(String.valueOf(t.getId()));
        for (TaskEntity subTask : findAllSubtasks) {
            deleteTaskRepeatDataEntity(subTask.getId());
            getCurrentSession().delete(subTask);
        }
        deleteTaskRepeatDataEntity(t.getId());
        getCurrentSession().delete(t);
    }

    //--------------------------------------------------------------------------
    // TASK REPEAT DATA
    //--------------------------------------------------------------------------
    public TaskRepeatDataEntity findRepeatDataByTaskId(Long id) {
        TaskRepeatDataEntity p = null;
        List list = getCurrentSession().createQuery("SELECT p FROM TaskRepeatDataEntity p WHERE p.task.id = :_id").setParameter("_id", id).list();
        if (list != null && !list.isEmpty()) {
            p = (TaskRepeatDataEntity) list.get(0);
        }
        return p;
    }

    public void saveOrUpdate(TaskRepeatDataEntity t) {
        getCurrentSession().saveOrUpdate(t);
        getCurrentSession().flush();
    }

    public List<TaskRepeatDataEntity> findRepeatableTasks() {
        List<CalendarEntity> findSelected = findSelectedCalendars();
        List<Long> findSelectedIds = Lambda.extract(findSelected, Lambda.on(CalendarEntity.class).getId());
        findSelectedIds.add(Long.MIN_VALUE);
        return getCurrentSession()
                .createQuery("SELECT p FROM TaskRepeatDataEntity p WHERE p.task.calendar.id IN (:_findSelectedIds)")
                .setParameterList("_findSelectedIds", findSelectedIds)
                .list();
    }

    public void deleteCurrentEvent(Long task_id, Date date) {
        TaskRepeatDataEntity rep = findRepeatDataByTaskId(task_id);
        if (rep != null) {
            EventException ee = new EventException();
            ee.setTaskRepeatDataEntity(rep);
            ee.setDateException(date);
            save(ee);
            rep.getEventsEx().add(ee);
            saveOrUpdate(rep);
        } else {
            deleteDateData(task_id);
        }
    }

    public void deleteTaskRepeatDataEntity(Long task_id) {
        TaskRepeatDataEntity rep = findRepeatDataByTaskId(task_id);
        if (rep != null) {
            deleteEventExceptions(rep);
            getCurrentSession().delete(rep);
        }
        deleteDateData(task_id);
    }

    public void save(EventException ee) {
        getCurrentSession().save(ee);
    }

    public List<EventException> findEventExceptions(TaskRepeatDataEntity te) {
        return getCurrentSession().createQuery("SELECT e FROM EventException e WHERE e.taskRepeatDataEntity.id = :_id")
                .setParameter("_id", te.getId())
                .list();
    }

    public void deleteEventExceptions(TaskRepeatDataEntity rep) {
        rep.setEventsEx(null);
        saveOrUpdate(rep);        
        getCurrentSession().createQuery("DELETE FROM EventException e WHERE e.taskRepeatDataEntity.id = :_rep_id")
                .setParameter("_rep_id", rep.getId())
                .executeUpdate();
    }

    //--------------------------------------------------------------------------
    // LINKS
    //--------------------------------------------------------------------------

    public void deleteLinks(TaskEntity task) {
        for (Iterator iterator1 = task.getLinks().iterator(); iterator1.hasNext();) {
            LinkEntity next = (LinkEntity) iterator1.next();
            iterator1.remove();
            saveOrUpdate(task);
            getCurrentSession().delete(next);
        }
    }

    //--------------------------------------------------------------------------
    // EVENTS
    //--------------------------------------------------------------------------
    public List<TaskEntity> findTasksBetween(Date start, Date end) {
        List<CalendarEntity> findSelected = findSelectedCalendars();
        List<Long> findSelectedIds = Lambda.extract(findSelected, Lambda.on(CalendarEntity.class).getId());
        findSelectedIds.add(Long.MIN_VALUE);
        return getCurrentSession()
                .createQuery("SELECT p FROM TaskEntity p"
                        + " WHERE ((p.start BETWEEN :_start and :_end) OR (p.end BETWEEN :_start and :_end)"
                        + " OR (:_start BETWEEN p.start and p.end) OR (:_end BETWEEN p.start and p.end))"
                        + " AND p.calendar.id IN (:_findSelectedIds)")
                .setParameter("_start", start)
                .setParameter("_end", end)
                .setParameterList("_findSelectedIds", findSelectedIds)
                .list();
    }

    
    //--------------------------------------------------------------------------
    // GROUP && SORT 
    //--------------------------------------------------------------------------
    public void saveOrUpdate(GroupSortEntity t) {
        getCurrentSession().saveOrUpdate(t);
    }

    public GroupSortEntity findGSBySelectedViewAndId(String view, String id) {
        return (GroupSortEntity) getCurrentSession()
                .createQuery("FROM GroupSortEntity gs WHERE gs.selectedView = :_selectedView AND gs.selectedId = :_selectedId")
                .setParameter("_selectedView", view)
                .setParameter("_selectedId", id)
                .uniqueResult();
    }

    public void deleteGroupAndSort(String view, String id) {
        getCurrentSession().createQuery("DELETE FROM GroupSortEntity gs WHERE gs.selectedView = :_selectedView AND gs.selectedId = :_selectedId")
                .setParameter("_selectedView", view)
                .setParameter("_selectedId", id)
                .executeUpdate();
    }

}

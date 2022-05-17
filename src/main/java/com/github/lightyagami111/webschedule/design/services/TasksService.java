/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.lightyagami111.webschedule.design.services;

import com.github.lightyagami111.webschedule.design.datastructure.CalendarEntity;
import com.github.lightyagami111.webschedule.design.datastructure.CalendarUIEventDTO;
import com.github.lightyagami111.webschedule.design.datastructure.GroupSortDTO;
import com.github.lightyagami111.webschedule.design.datastructure.GroupSortEntity;
import com.github.lightyagami111.webschedule.design.datastructure.LabelEntity;
import com.github.lightyagami111.webschedule.design.datastructure.ProjectEntity;
import com.github.lightyagami111.webschedule.design.datastructure.TaskEntity;
import com.github.lightyagami111.webschedule.design.datastructure.TaskRepeatDataDTO;
import com.github.lightyagami111.webschedule.design.datastructure.TaskRepeatDataEntity;
import com.github.lightyagami111.webschedule.design.datastructure.TaskRequestLoadDTO;
import com.github.lightyagami111.webschedule.design.datastructure.TaskRequestSaveDTO;
import com.github.lightyagami111.webschedule.design.datastructure.TaskTreeDTO;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author asd
 */
@Service
public class TasksService {
    
    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DaoService.class.getName());

    @Autowired
    private DaoService daoService;

    @Autowired
    private RepeatableService repeatableService;

    @Autowired
    private GroupAndSortService groupAndSortService;

    public List<GroupSortDTO> searchAndGroup(String searchTerm) {
        GroupSortEntity gs = groupAndSortService.getSearchGroupSort();
        List<TaskEntity> tasksSearch = daoService.search(searchTerm);

        List<TaskTreeDTO> tasksdto = new ArrayList<>();
        for (TaskEntity ts : tasksSearch) {
            tasksdto.add(taskEntityToTree(ts, gs));
        }

        return Arrays.asList(getGroupSortDTO(tasksdto, searchTerm, gs));
    }

    public List<GroupSortDTO> groupAndSort(GroupSortEntity gs, List<TaskEntity> tasks) {
        List<GroupSortDTO> result = new ArrayList<>();

        List<TaskTreeDTO> tasksdto = new ArrayList<>();
        for (TaskEntity task : tasks) {
            tasksdto.add(taskEntityToTree(task, gs));
        }

        if (gs.getGroup_().equals("group-label")) {
            LabelEntity noGroup = new LabelEntity();
            noGroup.setId(null);
            noGroup.setText("");
            Map<LabelEntity, List<TaskTreeDTO>> map = new HashMap<>();
            for (TaskTreeDTO task : tasksdto) {
                if (task.getLabels() != null && !task.getLabels().isEmpty()) {
                    for (LabelEntity key : task.getLabels()) {
                        List<TaskTreeDTO> value = map.get(key);
                        if (value == null) {
                            value = new ArrayList<>();
                        }
                        value.add(task);
                        map.put(key, value);
                    }
                } else {
                    List<TaskTreeDTO> value = map.get(noGroup);
                    if (value == null) {
                        value = new ArrayList<>();
                    }
                    value.add(task);
                    map.put(noGroup, value);
                }
            }

            for (Map.Entry<LabelEntity, List<TaskTreeDTO>> entry : map.entrySet()) {
                LabelEntity key = entry.getKey();
                List<TaskTreeDTO> value = entry.getValue();
                result.add(getGroupSortDTO(value, key.getText(), gs));
            }
            SortUtil.sortGroups(gs, result);

        } else if (gs.getGroup_().equals("group-project")) {
            Map<ProjectEntity, List<TaskTreeDTO>> map = new HashMap<>();
            for (TaskTreeDTO task : tasksdto) {
                ProjectEntity key = task.getProject();
                List<TaskTreeDTO> value = map.get(key);
                if (value == null) {
                    value = new ArrayList<>();
                }
                value.add(task);
                map.put(key, value);
            }
            for (Map.Entry<ProjectEntity, List<TaskTreeDTO>> entry : map.entrySet()) {
                ProjectEntity key = entry.getKey();
                List<TaskTreeDTO> value = entry.getValue();
                result.add(getGroupSortDTO(value, key.getText(), gs));
            }
            SortUtil.sortGroups(gs, result);

        } else if (gs.getGroup_().startsWith("group-sdate")) {
            Map<String, List<TaskTreeDTO>> map = new HashMap<>();
            String noDate = "";
            for (TaskTreeDTO task : tasksdto) {
                String key;
                if (task.getStartComputed() != null) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(task.getStartComputed());
                    c.set(Calendar.HOUR_OF_DAY, 0);
                    key = Utils.format(c.getTime(), "yyyy-MM-dd");
                } else {
                    key = noDate;
                }
                List<TaskTreeDTO> value = map.get(key);
                if (value == null) {
                    value = new ArrayList<>();
                }
                value.add(task);
                map.put(key, value);
            }

            for (Map.Entry<String, List<TaskTreeDTO>> entry : map.entrySet()) {
                String key = entry.getKey();
                if (key.equals(noDate)) {
                    continue;
                }
                List<TaskTreeDTO> value = entry.getValue();
                result.add(getGroupSortDTO(value, key, gs));
            }
            SortUtil.sortGroups(gs, result);
            //noDate
            List<TaskTreeDTO> value = map.get(noDate);
            if (value != null) {
                result.add(getGroupSortDTO(value, noDate, gs));
            }

        } else { // dont-group
            result.add(getGroupSortDTO(tasksdto, "", gs));
        }

        return result;
    }

    public GroupSortDTO getGroupSortDTO(List<TaskTreeDTO> list, String groupBy, GroupSortEntity gs) {
        GroupSortDTO groupProjectTasksDTO = new GroupSortDTO();
        groupProjectTasksDTO.setGroupBy(groupBy);
        SortUtil.sortTasks(gs, list);
        groupProjectTasksDTO.setTasks(list);
        return groupProjectTasksDTO;
    }

    public TaskTreeDTO taskEntityToTree(TaskEntity loadTask, GroupSortEntity gs) {
        TaskTreeDTO taskDTO = new TaskTreeDTO();
        Copier.copy(loadTask, taskDTO);
        if (groupAndSortService.disableParent(gs)) {
            taskDTO.setParent("#");
            taskDTO.setParent_text("");
        }

        TaskRepeatDataEntity taskRepEntity = daoService.findRepeatDataByTaskId(loadTask.getId());
        if (taskRepEntity != null) {
            taskDTO.setRep(taskRepEntity);
            taskDTO.setRepNextFire(repeatableService.computeNextFire(taskRepEntity));
        }

        return taskDTO;
    }

    public TaskRequestLoadDTO loadTaskData(Long task_id) {
        TaskRequestLoadDTO result = new TaskRequestLoadDTO();

        TaskEntity task = daoService.findTaskById(task_id);
        Copier.copy(task, result);

        String parent_id = String.valueOf(task.getId());
        List<TaskEntity> findAllSubtasks = daoService.findAllSubtasks(parent_id);
        for (TaskEntity findAllSubtask : findAllSubtasks) {
            if (findAllSubtask.getParent().equals(parent_id)) {
                findAllSubtask.setParent("#");
            }
        }
        result.setSubTasks(new HashSet(findAllSubtasks));

        result.setRepeatData(loadTaskRepeatData(task_id));

        return result;
    }

    public TaskRequestLoadDTO loadInitialTaskData(CalendarEntity findCalendarById, ProjectEntity findProjectById, String parent_id, Boolean insert, Long[] labels, Date start, Date end) {
        TaskRequestLoadDTO result = new TaskRequestLoadDTO();

        TaskEntity t = new TaskEntity();
        t.setProject(findProjectById);
        t.setCalendar(findCalendarById);

        t.setParent(parent_id);
        if (!parent_id.equals("#")) {
            t.setParent_text(daoService.findTaskById(Long.parseLong(parent_id)).getText());
        }

        t.setStart(start);
        t.setEnd(end);
        t.setAllDay(true);

        if (labels != null) {
            for (Long label_id : labels) {
                LabelEntity label = daoService.findLabelById(label_id);
                t.getLabels().add(label);
            }
        }

        t.setPriority(1);

        if (insert) {
            t.setId(daoService.save(t));
        }
        Copier.copy(t, result);

        result.setRepeatData(loadTaskRepeatData(null));

        return result;
    }

    public void saveTaskData(TaskRequestSaveDTO dTO, ProjectEntity project, CalendarEntity calendar) {
        TaskEntity t;
        if (dTO.getId() != null) {
            t = daoService.findTaskById(dTO.getId());
        } else {
            t = new TaskEntity();
        }

        if (dTO.getFromRepeatTaskId() != null) {
            daoService.deleteCurrentEvent(dTO.getFromRepeatTaskId(), Utils.parse(dTO.getFromRepeatTaskStart()));
        }

        t.setText(dTO.getText());

        t.setAllDay(dTO.getAllDay());
        if (dTO.getRepeatData() != null) {
            t.setStart(null);
            t.setEnd(null);
        } else {
            t.setStart(Utils.parse(dTO.getStart()));
            t.setEnd(Utils.parse(dTO.getEnd()));

            if (!t.getAllDay()) {
                if (t.getEnd() != null) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(t.getEnd());
                    int h = c.get(Calendar.HOUR);
                    int m = c.get(Calendar.MINUTE);

                    c.setTime(t.getStart());
                    c.set(Calendar.HOUR, h);
                    c.set(Calendar.MINUTE, m);

                    if (c.get(Calendar.HOUR) != 0 || c.get(Calendar.MINUTE) != 0) {
                        t.setEnd(c.getTime());
                    }
                    t.setEnd(c.getTime());
                }
            }
        }

        t.setParent(dTO.getParent());
        t.setParent_text(dTO.getParent_text());
        t.setProject(project);
        t.setCalendar(calendar);
        t.setNotes(dTO.getNotes());
        t.setPriority(dTO.getPriority());

        t.getLabels().clear();
        if (dTO.getLabels() != null) {
            for (Long label_id : dTO.getLabels()) {
                LabelEntity label = daoService.findLabelById(label_id);
                t.getLabels().add(label);
            }
        }
        daoService.deleteLinks(t);
        if (dTO.getLinks() != null && !dTO.getLinks().isEmpty()) {
            t.setLinks(new HashSet<>(dTO.getLinks()));
        }

        daoService.saveOrUpdate(t);
        saveTaskRepeatData(dTO.getRepeatData(), t);

        List<TaskEntity> subTasks = daoService.findAllSubtasks(String.valueOf(t.getId()));
        for (TaskEntity subTask : subTasks) {
            subTask.setProject(project);
            daoService.saveOrUpdate(subTask);
        }
    }

    public TaskRepeatDataDTO loadTaskRepeatData(Long task_id) {
        TaskRepeatDataDTO obj = new TaskRepeatDataDTO();

        TaskRepeatDataEntity t = null;
        if (task_id != null) {
            t = daoService.findRepeatDataByTaskId(task_id);
        }

        if (t != null) {
            obj.setEndson(t.getEndson());
            obj.setEndson_count(t.getEndson_count());
            obj.setEndson_until(Utils.format(t.getEndson_until()));
            obj.setMode(t.getMode());
            obj.setMode_start(Utils.format(t.getMode_start()));
            obj.setMode_end(Utils.format(t.getMode_end()));
            obj.setAllDay(t.getAllDay());
            obj.setRepeat_days(t.getRepeat_days());
            obj.setRepeat_months(t.getRepeat_months());
            obj.setRepeat_wdays(t.getRepeat_wdays_asArray());
            obj.setRepeat_weeks(t.getRepeat_weeks());
            obj.setRepeatby(t.getRepeatby());
            obj.setTask_id(t.getTask().getId());
        } else {
            obj.setTask_id(task_id);
            obj.setMode("d");
            obj.setEndson("never");
            obj.setRepeat_days(1);
            obj.setRepeat_weeks(1);
            obj.setRepeat_months(1);
            obj.setAllDay(Boolean.TRUE);
        }
        obj.setDbExist(t != null);

        return obj;
    }

    public void saveTaskRepeatData(TaskRepeatDataDTO dTO, TaskEntity task) {
        Long task_id = task.getId();
        TaskRepeatDataEntity t = daoService.findRepeatDataByTaskId(task_id);

        if (dTO == null) {
            if (t != null) {
                daoService.deleteTaskRepeatDataEntity(task_id);
            }
        } else {
            if (t == null) {
                t = new TaskRepeatDataEntity();
            } else {
                if (Utils.checkForChangesToDeleteEventExceptions(t, dTO)) {
                    daoService.deleteEventExceptions(t);
                }
            }

            t.setEndson(dTO.getEndson());
            t.setEndson_count(dTO.getEndson_count());
            t.setEndson_until(dTO.getEndson_until());
            t.setMode(dTO.getMode());
            t.setMode_start(dTO.getMode_start());
            t.setMode_end(dTO.getMode_end());
            t.setAllDay(dTO.getAllDay());
            t.setRepeat_days(dTO.getRepeat_days());
            t.setRepeat_months(dTO.getRepeat_months());
            t.setRepeat_wdays(String.join(",", dTO.getRepeat_wdays()));
            t.setRepeat_weeks(dTO.getRepeat_weeks());
            t.setRepeatby(dTO.getRepeatby());
            t.setTask(task);

            daoService.saveOrUpdate(t);
        }

    }

    public TaskRequestLoadDTO loadTaskRepeatDataCurrentEvent(Long task_id, Date start, Date end) {
        TaskEntity t = daoService.findTaskById(task_id);
        t.setId(null);
        t.setStart(start);
        t.setEnd(end);
        t.setParent(String.valueOf(task_id));
        t.setParent_text(t.getText());
        t.setAllDay(daoService.findRepeatDataByTaskId(task_id).getAllDay());

        TaskRequestLoadDTO result = new TaskRequestLoadDTO();
        Copier.copy(t, result);
        result.setRepeatData(loadTaskRepeatData(null));

        return result;
    }

    public TaskEntity updateTaskTitle(Long id, String new_title) {
        TaskEntity t = daoService.findTaskById(id);
        t.setText(new_title);
        daoService.saveOrUpdate(t);

        return t;
    }

    public List<CalendarUIEventDTO> loadEvents(Date start, Date end) {
        List<CalendarUIEventDTO> res = new ArrayList<>();

        List<TaskEntity> findTasksBetween = daoService.findTasksBetween(start, end);
        for (TaskEntity f : findTasksBetween) {
            CalendarUIEventDTO d = new CalendarUIEventDTO();
            d.setId(f.getId());
            d.setTitle(f.getText());
            d.setAllDay(f.getAllDay());
            d.setStart(Utils.format(f.getStart()));
            if (f.getEnd() != null) {
                Calendar c = Calendar.getInstance();
                c.setTime(f.getEnd());
                c.add(Calendar.DATE, 1);
                d.setEnd(Utils.format(c.getTime()));
            }
            d.setColor(f.getCalendar().getBckgColor());

            res.add(d);
        }

        List<TaskRepeatDataEntity> findRepeatableTasks = daoService.findRepeatableTasks();
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        if (!findRepeatableTasks.isEmpty()) {
            int numberOfRequests = findRepeatableTasks.size();
            List<RepeatableWorkThread> tasks = new ArrayList<>(numberOfRequests);
            for (int i = 0; i < numberOfRequests; i++) {
                TaskRepeatDataEntity findRepeatableTask = findRepeatableTasks.get(i);
                tasks.add(new RepeatableWorkThread(findRepeatableTask, start, end, repeatableService));
            }
            try {
                List<Future<List<CalendarUIEventDTO>>> futures = executorService.invokeAll(tasks);
                for (Future<List<CalendarUIEventDTO>> future : futures) {
                    List<CalendarUIEventDTO> result = future.get();
                    res.addAll(result);
                }

                executorService.shutdown();
            } catch (InterruptedException | ExecutionException ex) {
                log.error(ex.getMessage(), ex);
            }
        }

        return res;
    }

}

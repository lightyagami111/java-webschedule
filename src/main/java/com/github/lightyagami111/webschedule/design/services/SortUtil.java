/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.lightyagami111.webschedule.design.services;

import ch.lambdaj.Lambda;
import com.github.lightyagami111.webschedule.design.datastructure.GroupSortDTO;
import com.github.lightyagami111.webschedule.design.datastructure.GroupSortEntity;
import com.github.lightyagami111.webschedule.design.datastructure.TaskTreeDTO;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import org.hamcrest.Matchers;

/**
 *
 * @author asd
 */
public class SortUtil {

    public static void sortTasks(GroupSortEntity gs, List<TaskTreeDTO> list) {
        String sort_ = gs.getSort_();
        if (!sort_.equals("dont-sort")) {
            if (sort_.equals("sort-sdate-up")) {
                Collections.sort(list, Comparators.DATE_TASKS_UP);
                List<TaskTreeDTO> list1 = Lambda.select(list, Lambda.having(Lambda.on(TaskTreeDTO.class).getStartComputed(), Matchers.notNullValue()));
                List<TaskTreeDTO> list2 = Lambda.select(list, Lambda.having(Lambda.on(TaskTreeDTO.class).getStartComputed(), Matchers.nullValue()));
                list.clear();
                list.addAll(list1);
                list.addAll(list2);
            } else if (sort_.equals("sort-sdate-down")) {
                Collections.sort(list, Comparators.DATE_TASKS_DOWN);
                List<TaskTreeDTO> list1 = Lambda.select(list, Lambda.having(Lambda.on(TaskTreeDTO.class).getStartComputed(), Matchers.notNullValue()));
                List<TaskTreeDTO> list2 = Lambda.select(list, Lambda.having(Lambda.on(TaskTreeDTO.class).getStartComputed(), Matchers.nullValue()));
                list.clear();
                list.addAll(list1);
                list.addAll(list2);
            } else if (sort_.equals("sort-prio-up")) {
                Collections.sort(list, Comparators.PRIORITY_TASKS_UP);
            } else if (sort_.equals("sort-prio-down")) {
                Collections.sort(list, Comparators.PRIORITY_TASKS_DOWN);
            }
        }
    }

    public static void sortGroups(GroupSortEntity gs, List<GroupSortDTO> list) {
        String group_ = gs.getGroup_();
        if (group_.equals("group-sdate-up")) {
            Collections.sort(list, Comparators.DATE_GROUP_UP);
        } else if (group_.equals("group-sdate-down")) {
            Collections.sort(list, Comparators.DATE_GROUP_DOWN);
        } else {
            Collections.sort(list, Comparators.GROUP_DOWN);
        }
    }

}

class Comparators {

    public static Comparator<TaskTreeDTO> PRIORITY_TASKS_UP = new Comparator<TaskTreeDTO>() {
        @Override
        public int compare(TaskTreeDTO o1, TaskTreeDTO o2) {
            int result = 0;
            if (o1.getPriority() == null) {
                if (o2.getPriority() != null) {
                    result = -1;
                }
            } else if (o2.getPriority() == null) {
                result = 1;
            } else {
                result = o1.getPriority().compareTo(o2.getPriority());
            }
            return result;
        }
    };

    public static Comparator<TaskTreeDTO> PRIORITY_TASKS_DOWN = new Comparator<TaskTreeDTO>() {
        @Override
        public int compare(TaskTreeDTO o1, TaskTreeDTO o2) {
            int result = 0;
            if (o1.getPriority() == null) {
                if (o2.getPriority() != null) {
                    result = 1;
                }
            } else if (o2.getPriority() == null) {
                result = -1;
            } else {
                result = o2.getPriority().compareTo(o1.getPriority());
            }
            return result;
        }
    };

    public static Comparator<TaskTreeDTO> DATE_TASKS_UP = new Comparator<TaskTreeDTO>() {
        @Override
        public int compare(TaskTreeDTO o1, TaskTreeDTO o2) {
            int result = 0;
            if (o1.getStartComputed()== null) {
                if (o2.getStartComputed() != null) {
                    result = -1;
                }
            } else if (o2.getStartComputed() == null) {
                result = 1;
            } else {
                result = o1.getStartComputed().compareTo(o2.getStartComputed());
            }
            return result;
        }
    };

    public static Comparator<TaskTreeDTO> DATE_TASKS_DOWN = new Comparator<TaskTreeDTO>() {
        @Override
        public int compare(TaskTreeDTO o1, TaskTreeDTO o2) {
            int result = 0;
            if (o1.getStartComputed() == null) {
                if (o2.getStartComputed() != null) {
                    result = 1;
                }
            } else if (o2.getStartComputed() == null) {
                result = -1;
            } else {
                result = o2.getStartComputed().compareTo(o1.getStartComputed());
            }
            return result;
        }
    };

    public static Comparator<GroupSortDTO> DATE_GROUP_UP = new Comparator<GroupSortDTO>() {
        @Override
        public int compare(GroupSortDTO o1, GroupSortDTO o2) {
            Date d1 = Utils.parse(o1.getGroupBy());
            Date d2 = Utils.parse(o2.getGroupBy());

            int result = 0;
            if (d1 == null) {
                if (d2 != null) {
                    result = -1;
                }
            } else if (d2 == null) {
                result = 1;
            } else {
                result = d1.compareTo(d2);
            }
            return result;
        }
    };

    public static Comparator<GroupSortDTO> DATE_GROUP_DOWN = new Comparator<GroupSortDTO>() {
        @Override
        public int compare(GroupSortDTO o1, GroupSortDTO o2) {
            Date d1 = Utils.parse(o1.getGroupBy());
            Date d2 = Utils.parse(o2.getGroupBy());

            int result = 0;
            if (d1 == null) {
                if (d2 != null) {
                    result = 1;
                }
            } else if (d2 == null) {
                result = -1;
            } else {
                result = d2.compareTo(d1);
            }
            return result;
        }
    };

    public static Comparator<GroupSortDTO> GROUP_DOWN = new Comparator<GroupSortDTO>() {
        @Override
        public int compare(GroupSortDTO o1, GroupSortDTO o2) {
            int result = 0;
            if (o1.getGroupBy() == null) {
                if (o2.getGroupBy() != null) {
                    result = 1;
                }
            } else if (o2.getGroupBy() == null) {
                result = -1;
            } else {
                result = o2.getGroupBy().compareTo(o1.getGroupBy());
            }
            return result;
        }
    };
}

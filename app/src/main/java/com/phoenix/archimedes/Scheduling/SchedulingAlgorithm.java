package com.phoenix.archimedes.Scheduling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class SchedulingAlgorithm {
    Map<Integer, String> slots = new HashMap<Integer, String>();

    public SchedulingAlgorithm() {
        slots.put(910, "free");
        slots.put(1011, "free");
        slots.put(1213, "free");
        slots.put(1314, "free");
        slots.put(1415, "free");
        slots.put(1516, "free");
        slots.put(1617, "free");
        slots.put(1718, "free");
        slots.put(1819, "free");
        slots.put(1920, "free");
        slots.put(2021, "free");
    }

    public Map<Integer, String> schedule(ArrayList<Integer> classschedule, Map<String, Integer> activites) {

        Set<Entry<Integer, String>> st = slots.entrySet();

        for (Entry<Integer, String> me : st) {
            for (int i = 0; i < classschedule.size(); i++) {
                Integer key = me.getKey();
                int cls = classschedule.get(i);
                if (cls == key) {
                    slots.put(key, "schedulded");
                }

            }

        }

        ArrayList<Integer> freeslots = new ArrayList<Integer>();
        for (Entry<Integer, String> me : st) {

            String val = me.getValue();
            if (val.equals("free")) {
                int key = me.getKey();
                freeslots.add(key);
            }
        }


        Set<Entry<String, Integer>> st1 = activites.entrySet();

        for (Entry<String, Integer> me : st1) {

            String key = me.getKey();
            int hrs = me.getValue();
            if (hrs > 0) {
                for (int k = 0; k < hrs; k++) {
                    try {
                        int slot = freeslots.get(0);
                        slots.put(slot, key);
                        activites.put(key, hrs - 1);
                        freeslots.remove(0);
                    } catch (Exception e) {
                        break;
                    }
                }
            }
        }

        return slots;

    }
}

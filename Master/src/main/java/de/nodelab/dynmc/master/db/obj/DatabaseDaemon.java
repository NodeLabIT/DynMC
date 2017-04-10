package de.nodelab.dynmc.master.db.obj;

import lombok.Data;
import lombok.Getter;
import org.bson.Document;

@Data
public class DatabaseDaemon extends DatabaseObject {

    private String name;
    private boolean online;

    private String key;
    private String host;

    private int cpu;
    private int ram;

    private int minPort;
    private int maxPort;

    @Override
    public Document convertToBson() {
        return new Document("name", this.name)
                .append("online", this.online)
                .append("key", this.key)
                .append("host", this.host)
                .append("cpu", this.cpu)
                .append("ram", this.ram)
                .append("minPort", this.minPort)
                .append("maxPort", this.maxPort);
    }

    @Override
    public void fillFromBson(Document document) {
        this.name = document.getString("name");
        this.online = document.getBoolean("online");
        this.key = document.getString("key");
        this.host = document.getString("host");
        this.cpu = document.getInteger("cpu");
        this.ram = document.getInteger("ram");
        this.minPort = document.getInteger("minPort");
        this.maxPort = document.getInteger("maxPort");
    }

    public Document getUpdateDocument(DatabaseDaemon.AccessRule... rules) {
        Document set = new Document();

        for(DatabaseDaemon.AccessRule rule : rules) {
            switch(rule) {
                case NAME:
                    set.append("name", this.name);
                    break;
                case ONLINE:
                    set.append("online", this.online);
                    break;
                case KEY:
                    set.append("key", this.key);
                    break;
                case HOST:
                    set.append("host", this.host);
                    break;
                case CPU:
                    set.append("cpu", this.cpu);
                    break;
                case RAM:
                    set.append("ram", this.ram);
                    break;
                case MIN_PORT:
                    set.append("minPort", this.minPort);
                    break;
                case MAX_PORT:
                    set.append("maxPort", this.maxPort);
                    break;
                case ALL:
                    return this.convertToBson();
            }
        }

        return new Document("$set", set);
    }

    public enum AccessRule {
        NAME("name"), ONLINE("online"), KEY("key"), HOST("host"), CPU("cpu"), RAM("ram"), MIN_PORT("minPort"), MAX_PORT("maxPort"), ALL("");

        @Getter
        private String field;

        AccessRule(String field) {
            this.field = field;
        }

        public static String[] toList(DatabaseDaemon.AccessRule... rules) {
            String[] list = new String[rules.length];
            for(int i = 0; i < rules.length; i++) {
                list[i] = rules[i].getField();
            }
            return list;
        }

        public static boolean containsAll(DatabaseDaemon.AccessRule... rules) {
            boolean all = false;
            for(DatabaseDaemon.AccessRule rule : rules) {
                if(rule == DatabaseDaemon.AccessRule.ALL) {
                    all = true;
                }
            }
            return all;
        }
    }

}

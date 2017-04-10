package de.nodelab.dynmc.master.db.obj;

import lombok.Data;
import lombok.Getter;
import org.bson.Document;

@Data
public class DatabaseServer extends DatabaseObject {

    private String name;
    private String serverType;
    private String daemon;
    private int allocatedRAM;

    private int online;
    private int maxPlayers;

    @Override
    public Document convertToBson() {
        return new Document("name", this.name)
                .append("servertype", this.serverType)
                .append("daemon", this.daemon)
                .append("allocatedRAM", this.allocatedRAM)
                .append("online", this.online)
                .append("maxPlayers", this.maxPlayers);
    }

    @Override
    public void fillFromBson(Document bson) {
        this.name = bson.getString("name");
        this.serverType = bson.getString("serverType");
        this.daemon = bson.getString("daemon");
        this.allocatedRAM = bson.getInteger("allocatedRAM");
        this.online = bson.getInteger("online");
        this.maxPlayers = bson.getInteger("maxPlayers");
    }

    public Document getUpdateDocument(DatabaseServer.AccessRule... rules) {
        Document set = new Document();

        for(DatabaseServer.AccessRule rule : rules) {
            switch(rule) {
                case NAME:
                    set.append("name", this.name);
                    break;
                case SERVERTYPE:
                    set.append("servertype", this.serverType);
                    break;
                case DAEMON:
                    set.append("daemon", this.daemon);
                    break;
                case ALLOCATED_RAM:
                    set.append("allocatedRAM", this.allocatedRAM);
                    break;
                case ONLINE:
                    set.append("online", this.online);
                    break;
                case MAX_PLAYERS:
                    set.append("maxPlayers", this.maxPlayers);
                    break;
                case ALL:
                    return this.convertToBson();
            }
        }

        return new Document("$set", set);
    }

    public enum AccessRule {
        NAME("name"), SERVERTYPE("serverType"), DAEMON("daemon"), ALLOCATED_RAM("allocatedRAM"), ONLINE("online"), MAX_PLAYERS("maxPlayers"), ALL("");

        @Getter
        private String field;

        AccessRule(String field) {
            this.field = field;
        }

        public static String[] toList(DatabaseServer.AccessRule... rules) {
            String[] list = new String[rules.length];
            for(int i = 0; i < rules.length; i++) {
                list[i] = rules[i].getField();
            }
            return list;
        }

        public static boolean containsAll(DatabaseServer.AccessRule... rules) {
            boolean all = false;
            for(DatabaseServer.AccessRule rule : rules) {
                if(rule == DatabaseServer.AccessRule.ALL) {
                    all = true;
                }
            }
            return all;
        }
    }

}

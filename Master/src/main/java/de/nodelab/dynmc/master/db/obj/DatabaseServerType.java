package de.nodelab.dynmc.master.db.obj;

import lombok.Data;
import lombok.Getter;
import org.bson.Document;

@Data
public class DatabaseServerType extends DatabaseObject {

    private String name;
    private String[] plugins;
    private String[] worlds;

    private boolean manual;

    @Override
    public Document convertToBson() {
        return new Document("name", this.name)
                .append("plugins", this.plugins)
                .append("worlds", this.worlds)
                .append("manual", this.manual);
    }

    @Override
    public void fillFromBson(Document bson) {
        this.name = bson.getString("name");
        this.plugins = (String[]) bson.get("plugins");
        this.worlds = (String[]) bson.get("worlds");
        this.manual = bson.getBoolean("manual");
    }

    public Document getUpdateDocument(DatabaseServerType.AccessRule... rules) {
        Document set = new Document();

        for(DatabaseServerType.AccessRule rule : rules) {
            switch(rule) {
                case NAME:
                    set.append("name", this.name);
                    break;
                case PLUGINS:
                    set.append("plugins", this.plugins);
                    break;
                case WORLDS:
                    set.append("worlds", this.worlds);
                    break;
                case MANUAL:
                    set.append("manual", this.manual);
                    break;
                case ALL:
                    return this.convertToBson();
            }
        }

        return new Document("$set", set);
    }

    public enum AccessRule {
        NAME("name"), PLUGINS("plugins"), WORLDS("worlds"), MANUAL("manual"), ALL("");

        @Getter
        private String field;

        AccessRule(String field) {
            this.field = field;
        }

        public static String[] toList(DatabaseServerType.AccessRule... rules) {
            String[] list = new String[rules.length];
            for(int i = 0; i < rules.length; i++) {
                list[i] = rules[i].getField();
            }
            return list;
        }

        public static boolean containsAll(DatabaseServerType.AccessRule... rules) {
            boolean all = false;
            for(DatabaseServerType.AccessRule rule : rules) {
                if(rule == DatabaseServerType.AccessRule.ALL) {
                    all = true;
                }
            }
            return all;
        }
    }

}

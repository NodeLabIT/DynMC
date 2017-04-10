package de.nodelab.dynmc.master.db.obj;

import lombok.Data;
import lombok.Getter;
import org.bson.Document;

@Data
public class DatabaseWorld extends DatabaseObject {

    private String name;
    private String description;

    @Override
    public Document convertToBson() {
        return new Document("name", this.name)
                .append("description", this.description);
    }

    @Override
    public void fillFromBson(Document bson) {
        this.name = bson.getString("name");
        this.description = bson.getString("description");
    }

    public Document getUpdateDocument(AccessRule... rules) {
        Document set = new Document();

        for(AccessRule rule : rules) {
            switch(rule) {
                case NAME:
                    set.append("name", this.name);
                    break;
                case DESCRIPTION:
                    set.append("description", this.description);
                    break;
                case ALL:
                    return this.convertToBson();
            }
        }

        return new Document("$set", set);
    }

    public enum AccessRule {
        NAME("name"), DESCRIPTION("description"), ALL("");

        @Getter
        private String field;

        AccessRule(String field) {
            this.field = field;
        }

        public static String[] toList(AccessRule... rules) {
            String[] list = new String[rules.length];
            for(int i = 0; i < rules.length; i++) {
                list[i] = rules[i].getField();
            }
            return list;
        }

        public static boolean containsAll(AccessRule... rules) {
            boolean all = false;
            for(AccessRule rule : rules) {
                if(rule == AccessRule.ALL) {
                    all = true;
                }
            }
            return all;
        }
    }

}

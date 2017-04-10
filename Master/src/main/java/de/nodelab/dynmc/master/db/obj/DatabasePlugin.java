package de.nodelab.dynmc.master.db.obj;

import lombok.Data;
import lombok.Getter;
import org.bson.Document;

@Data
public class DatabasePlugin extends DatabaseObject {

    private String name;
    private String version;
    private int size;
    private String description;
    private String author;

    @Override
    public Document convertToBson() {
        return new Document("name", this.name)
                .append("version", this.name)
                .append("size", this.size)
                .append("description", this.description);
    }

    @Override
    public void fillFromBson(Document bson) {
        this.name = bson.getString("name");
        this.version = bson.getString("version");
        this.size = bson.getInteger("size");
        this.description = bson.getString("description");
        this.author = bson.getString("author");
    }

    public Document getUpdateDocument(DatabasePlugin.AccessRule... rules) {
        Document set = new Document();

        for(DatabasePlugin.AccessRule rule : rules) {
            switch(rule) {
                case NAME:
                    set.append("name", this.name);
                    break;
                case VERSION:
                    set.append("version", this.version);
                    break;
                case SIZE:
                    set.append("size", this.size);
                    break;
                case DESCRIPTION:
                    set.append("description", this.description);
                    break;
                case AUTHOR:
                    set.append("author", this.author);
                    break;
                case ALL:
                    return this.convertToBson();
            }
        }

        return new Document("$set", set);
    }

    public enum AccessRule {
        NAME("name"), VERSION("version"), SIZE("size"), DESCRIPTION("description"), AUTHOR("author"), ALL("");

        @Getter
        private String field;

        AccessRule(String field) {
            this.field = field;
        }

        public static String[] toList(DatabasePlugin.AccessRule... rules) {
            String[] list = new String[rules.length];
            for(int i = 0; i < rules.length; i++) {
                list[i] = rules[i].getField();
            }
            return list;
        }

        public static boolean containsAll(DatabasePlugin.AccessRule... rules) {
            boolean all = false;
            for(DatabasePlugin.AccessRule rule : rules) {
                if(rule == DatabasePlugin.AccessRule.ALL) {
                    all = true;
                }
            }
            return all;
        }
    }

}

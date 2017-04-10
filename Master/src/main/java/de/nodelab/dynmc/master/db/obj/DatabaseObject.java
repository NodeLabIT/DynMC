package de.nodelab.dynmc.master.db.obj;

import org.bson.Document;

public abstract class DatabaseObject {

    public abstract Document convertToBson();

    public abstract void fillFromBson(Document bson);

}

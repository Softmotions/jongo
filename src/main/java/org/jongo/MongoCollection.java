/*
 * Copyright (C) 2011 Benoit GUEROUT <bguerout at gmail dot com> and Yves AMSELLEM <amsellem dot yves at gmail dot com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jongo;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;
import org.bson.types.ObjectId;
import org.jongo.marshall.Marshaller;
import org.jongo.marshall.Unmarshaller;

import java.util.Iterator;
import java.util.List;

import static org.jongo.Jongo.toDBObject;
import static org.jongo.ResultMapperFactory.newMapper;

public class MongoCollection {

    public static final String MONGO_ID = "_id";
    private final DBCollection collection;
    private final Marshaller marshaller;
    private final Unmarshaller unmarshaller;

    public MongoCollection(DBCollection dbCollection, Marshaller marshaller, Unmarshaller unmarshaller) {
        this.collection = dbCollection;
        this.marshaller = marshaller;
        this.unmarshaller = unmarshaller;
    }

    public FindOne findOne(String query) {
        return new FindOne(unmarshaller, collection, new Query(query));
    }

    public FindOne findOne(ObjectId id) {
        return new FindOne(unmarshaller, collection, new Query("{_id:#}", id));
    }

    public FindOne findOne(String query, Object... parameters) {
        return new FindOne(unmarshaller, collection, new Query(query, parameters));
    }

    public Find find(String query) {
        return new Find(unmarshaller, collection, new Query(query));
    }

    public Find find(String query, Object... parameters) {
        return new Find(unmarshaller, collection, new Query(query, parameters));
    }

    public long count(String query) {
        return collection.count(new Query(query).toDBObject());
    }

    public long count(String query, Object... parameters) {
        return collection.count(new Query(query, parameters).toDBObject());
    }

    public WriteResult update(String query, String modifier) {
        return collection.update(toDBObject(query), toDBObject(modifier), false, true);
    }

    public <D> String save(D document) {
        return save(document, collection.getWriteConcern());
    }

    public <D> String save(D document, WriteConcern concern) {
        String entityAsJson = marshaller.marshall(document);
        DBObject dbObject = toDBObject(entityAsJson);
        collection.save(dbObject, concern);
        return dbObject.get(MONGO_ID).toString();
    }

    public WriteResult insert(String query) {
        return collection.save(new Query(query).toDBObject());
    }

    public WriteResult insert(String query, Object... parameters) {
        return collection.save(new Query(query, parameters).toDBObject());
    }

    public WriteResult remove(String query) {
        return collection.remove(toDBObject(query));
    }

    public WriteResult remove(String query, Object... parameters) {
        return collection.remove(new Query(query, parameters).toDBObject());
    }

    public WriteResult remove(ObjectId id) {
        return remove("{_id:#}", id);
    }

    @SuppressWarnings("unchecked")
    public <T> Iterable<T> distinct(String key, String query, final Class<T> clazz) {
        DBObject ref = new Query(query).toDBObject();
        final List<?> distinct = collection.distinct(key, ref);
        if (BSONPrimitives.contains(clazz))
            return new Iterable<T>() {
                public Iterator<T> iterator() {
                    return (Iterator<T>) distinct.iterator();
                }
            };
        else
            return new MongoIterator<T>((Iterator<DBObject>) distinct.iterator(), newMapper(clazz, unmarshaller));
    }

    public void drop() {
        collection.drop();
    }

    public void ensureIndex(String index) {
        collection.ensureIndex(toDBObject(index));
    }

    public String getName() {
        return collection.getName();
    }

    public DBCollection getDBCollection() {
        return collection;
    }

}
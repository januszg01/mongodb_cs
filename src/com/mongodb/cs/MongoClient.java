package com.mongodb.cs;
 
import java.net.UnknownHostException;
import java.util.List;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MapReduceCommand;
import com.mongodb.MapReduceOutput;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.util.JSON;


public class MongoClient {


	private DBCollection loadDB (List<Contributor> contributors) throws UnknownHostException, MongoException {

		DBCollection collection = null;

		Mongo mongo = new Mongo();
		DB db = mongo.getDB("mongoCS");
		collection = db.getCollection("contibutors");

		// convert list of contributors to JSON format and insert to MongoDB collection
		Gson gson = new Gson();
		for(Contributor contributor: contributors) {
			String jsonString = gson.toJson(contributor);
			DBObject dbObject = (DBObject) JSON.parse(jsonString);
			collection.insert(dbObject);
		}

		// display content of collection
		System.out.println("\nContributors collection: ");
		DBCursor cursorDoc = collection.find();
		while (cursorDoc.hasNext()) {
			System.out.println(cursorDoc.next());
		}

		return collection;
	}



	private void search (DBCollection collection, String element, String value) {	

		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put(element, value);

		System.out.println("\nFound: ");
		DBCursor cursor = collection.find(searchQuery);
		while (cursor.hasNext()) {
			System.out.println(cursor.next());
		}
	}



	/**
	 * Using map reduce to show number of commits made by each user
	 * @param collection
	 * @throws MongoException
	 */
	private void userCommits (DBCollection collection) throws MongoException {	


		// group by - Map Reduce
		String map = "function() { "+ 
				"var category; " +  
				"category = this.userId; " +
				"emit(category, {userId: this.userId});}";

		String reduce = "function(key, values) { " +
				"var sum = 0; " +
				"values.forEach(function(doc) { " +
				"sum += 1; "+
				"}); " +
				"return {count:sum};} ";
		

		MapReduceCommand cmd = new MapReduceCommand(collection, map, reduce,
				null, MapReduceCommand.OutputType.INLINE, null);

		MapReduceOutput out = collection.mapReduce(cmd);

		System.out.println("\nMap Reduce - User Commits: ");
		for (DBObject o : out.results()) {
			System.out.println(o.toString());
		}

	}
	
	
	/**
	 * Map reduce showing commits made for each user
	 * @param collection
	 * @throws MongoException
	 */
	private void projectCommits (DBCollection collection) throws MongoException {	


		// group by - Map Reduce
		String map = "function() { "+ 
				"var category; " +  
				"category = this.repoName; " +
				"emit(category, {repository: this.repoName});}";

		String reduce = "function(key, values) { " +
				"var sum = 0; " +
				"values.forEach(function(doc) { " +
				"sum += 1; "+
				"}); " +
				"return {count:sum};} ";
		

		MapReduceCommand cmd = new MapReduceCommand(collection, map, reduce,
				null, MapReduceCommand.OutputType.INLINE, null);

		MapReduceOutput out = collection.mapReduce(cmd);

		System.out.println("\nMap Reduce - Project Commits: ");
		for (DBObject o : out.results()) {
			System.out.println(o.toString());
		}

	}




	public static void main(String[] args) {


		MongoClient mongoClient = new MongoClient();
		DBCollection collection = null;

		try {
			List<Contributor> contributors = ContributorsData.generateData();
			collection = mongoClient.loadDB(contributors);
			mongoClient.search(collection, "userId", "id0");
			mongoClient.userCommits(collection);
			mongoClient.projectCommits(collection);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			collection.drop();
		}

	}




}

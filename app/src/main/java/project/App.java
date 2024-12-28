/*
 * This is the main class which initializes the Calculator and calls the functions to calculation the average degree
 */
package project;

import java.io.IOException;

public class App {

    public static void main(String[] args) throws IOException {
        System.out.println("Initializing the graph with the tweet data ...\n");
        DegreeCalculator engine = new DegreeCalculator();

        double averageDegree = engine.calculateAverage();
        System.out.println("Average Degree of the entire tweets.txt file : " + averageDegree+"\n\n");

        engine.addTweet("{\"id_str\":\"tweet11\",\"entities\":{\"hashtags\":[{\"text\":\"Gretel\"},{\"text\":\"data\"}]}}");
        averageDegree = engine.calculateAverage();
        System.out.println( "Added tweet 11. Average after the addition: "+ averageDegree+"\n\n");
        
        engine.addTweet("{\"id_str\":\"tweet12\",\"entities\":{\"hashtags\":[{\"text\":\"dogs\"},{\"text\":\"tiger\"}]}}");
        averageDegree = engine.calculateAverage();
        System.out.println( "Added tweet 12. Average after the addition: "+ averageDegree+"\n\n");

        engine.addTweet("{\"id_str\":\"tweet13\",\"entities\":{\"hashtags\":[{\"text\":\"dogs\"},{\"text\":\"tiger\"}]}}");
        averageDegree = engine.calculateAverage();
        System.out.println( "Added tweet 13 (Same hash Tags as tweet 12). Average after the addition: "+ averageDegree+"\n\n");

        engine.removeTweet("{\"id_str\":\"tweet13\",\"entities\":{\"hashtags\":[{\"text\":\"dogs\"},{\"text\":\"tiger\"}]}}");
        averageDegree = engine.calculateAverage();
        System.out.println( "Removed tweet 13. Average after the Removal: "+ averageDegree+"\n\n");

        engine.removeTweet("{\"id_str\":\"tweet12\",\"entities\":{\"hashtags\":[{\"text\":\"dogs\"},{\"text\":\"tiger\"}]}}");
        averageDegree = engine.calculateAverage();
        System.out.println( "Removed tweet 12. Average after the Removal: "+ averageDegree+"\n\n");

        engine.removeTweet("{\"id_str\":\"tweet11\",\"entities\":{\"hashtags\":[{\"text\":\"Gretel\"},{\"text\":\"data\"}]}}");
        averageDegree = engine.calculateAverage();
        System.out.println( "Removed tweet 11. Average after the Removal: "+ averageDegree+"\n\n");
    }
}

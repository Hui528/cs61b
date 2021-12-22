package bearmaps.proj2d;

import bearmaps.proj2ab.KDTree;
import bearmaps.proj2ab.Point;
import bearmaps.proj2ab.PointSet;
import bearmaps.proj2ab.WeirdPointSet;
import bearmaps.proj2c.streetmap.StreetMapGraph;
import bearmaps.proj2c.streetmap.Node;

import java.util.*;

/**
 * An augmented graph that is more powerful that a standard StreetMapGraph.
 * Specifically, it supports the following additional operations:
 *
 *
 * @author Alan Yao, Josh Hug, Hui Wang
 */
public class AugmentedStreetMapGraph extends StreetMapGraph {

    private List<Node> nodes;
    private List<Node> allNodesWithName;
    private List<String> allNodesNames;
    private Map<String, List<String>> cleanedToNames;

    public AugmentedStreetMapGraph(String dbPath) {
        super(dbPath);
        // You might find it helpful to uncomment the line below:
        nodes = this.getNodes();
        allNodesWithName = getAllNodesWithName(nodes);
        allNodesNames = getAllNodesNames(allNodesWithName);
        cleanedToNames = getCleanedToNames(allNodesNames);
    }


    /**
     * For Project Part II
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    public long closest(double lon, double lat) {
        List<Node> updatedNodes = allNodesWithEdge(nodes);
        Map<Point, Long> pointsWithName = NodeToPoint(updatedNodes);
        List<Point> points = new ArrayList<>();
        for(Point p : pointsWithName.keySet()) {
            points.add(p);
        }
        KDTree possibleList = new KDTree(points);
        //PointSet possibleList = new WeirdPointSet(points);
        Point closestP = possibleList.nearest(lon, lat);
        //Point closestP = possibleList.nearest(lon, lat);
        return pointsWithName.get(closestP);
    }


    /**
     * For Project Part III (gold points)
     * In linear time, collect all the names of OSM locations that prefix-match the query string.
     * @param prefix Prefix string to be searched for. Could be any case, with our without
     *               punctuation.
     * @return A <code>List</code> of the full names of locations whose cleaned name matches the
     * cleaned <code>prefix</code>.
     */
    public List<String> getLocationsByPrefix(String prefix) {
        return cleanedToNames.get(prefix);
    }

    /**
     * For Project Part III (gold points)
     * Collect all locations that match a cleaned <code>locationName</code>, and return
     * information about each node that matches.
     * @param locationName A full name of a location searched for.
     * @return A list of locations whose cleaned name matches the
     * cleaned <code>locationName</code>, and each location is a map of parameters for the Json
     * response as specified: <br>
     * "lat" -> Number, The latitude of the node. <br>
     * "lon" -> Number, The longitude of the node. <br>
     * "name" -> String, The actual name of the node. <br>
     * "id" -> Number, The id of the node. <br>
     */
    public List<Map<String, Object>> getLocations(String locationName) {
        List<Map<String, Object>> list = new LinkedList<>();
        Map<String, List<Node>> namesToNodes = getNamesToNodes(allNodesWithName);
        List<Node> locations = namesToNodes.get(locationName);
        for(Node location : locations) {
            Map<String, Object> temp = new HashMap<>();
            temp.put("lat", location.lat());
            temp.put("lon", location.lon());
            temp.put("name", location.name());
            temp.put("id", location.id());
            list.add(temp);
        }
        return list;
    }


    /**
     * Useful for Part III. Do not modify.
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    private static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    private List<Node> allNodesWithEdge(List<Node> nodes) {
        List<Node> updatedNodes = new ArrayList<>();
        for(Node node : nodes) {
            if(neighbors(node.id()).size() != 0) {
                updatedNodes.add(node);
            }
        }
        return updatedNodes;
    }

    private Map<Point, Long> NodeToPoint(List<Node> nodes) {
        Map<Point, Long> pointsWithName = new HashMap<>();
        for(Node node : nodes) {
            pointsWithName.put(new Point(node.lon(), node.lat()), node.id());
        }
        return pointsWithName;
    }

    /*
    private String cleanedName(String name) {
        String cleanedName = "";
        for(int i = 0; i < name.length(); i++) {
            char next = name.charAt(i);
            if(next != ' ' && (next < 65 || (next > 90 && next < 97) || next > 123)) {
                continue;
            }
            cleanedName = cleanedName + next;
        }
        cleanedName = cleanedName.toLowerCase();
        return cleanedName;
    }

     */

    private List<Node> getAllNodesWithName(List<Node> nodes) {
        List<Node> allNodesWithName = new ArrayList<>();
        ListIterator<Node> listOfNodes = nodes.listIterator();
        while(listOfNodes.hasNext()) {
            Node temp = listOfNodes.next();
            String words = name(temp.id());
            if(words != null) {
                allNodesWithName.add(temp);
            }

        }
        return allNodesWithName;
    }

    private List<String> getAllNodesNames(List<Node> nodes) {
        List<String> allNodesNames = new ArrayList<>();
        ListIterator<Node> listOfNodes = nodes.listIterator();
        while(listOfNodes.hasNext()) {
            allNodesNames.add(listOfNodes.next().name());
        }
        return allNodesNames;
    }

    private Map<String, List<String>> getCleanedToNames(List<String> allWords) {
        Map<String, List<String>> cleanToNames = new HashMap<>();
        ListIterator<String> listOfAllWords = allWords.listIterator();
        while(listOfAllWords.hasNext()) {
            String words = listOfAllWords.next();
            for(int i = 1; i < words.length() + 1; i++) {
                String subWords = words.substring(0, i);   // (0, i]
                String cleanedPrefix = cleanString(subWords);
                if(cleanedPrefix == "") {
                    continue;
                }
                if(!cleanToNames.containsKey(cleanedPrefix)) {
                    cleanToNames.put(cleanedPrefix, new LinkedList<>());
                }
                List<String> temp = cleanToNames.get(cleanedPrefix);
                if(!temp.contains(words)) {
                    temp.add(words);
                }
            }
        }
        return cleanToNames;
    }

    private Map<String, List<Node>> getNamesToNodes(List<Node> allWords){
        Map<String, List<Node>> namesToNodes = new HashMap<>();
        ListIterator<Node> list = allWords.listIterator();
        while(list.hasNext()) {
            Node next = list.next();
            if(!namesToNodes.containsKey(next.name())) {
                namesToNodes.put(next.name(), new LinkedList<>());
            }
            namesToNodes.get(next.name()).add(next);
        }
        return namesToNodes;
    }
}

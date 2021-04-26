package DS.function;
import Page.Page4;

import DS.common.*;
import DS.function.stratrgyPack.*;
import java.util.*;
import MyLog.Mylog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Navigator {
    final Logger logger = LoggerFactory.getLogger(Navigator.class);
    private final Graph g;
    private int beginNum;
    private int endNum;
    private Vector<Edge> route;
    private Strategy strategy;
    private ArrayList<Integer> WayToPoint;
    private Vector<Edge> buffer;
    private int cannotApproach;
    private boolean setBeginNum;

    public Navigator(){ g = new Graph(); }
    public Navigator(Graph g1){
        g = g1;
        route = new Vector<>();
        buffer = new Vector<>();
        setBeginNum = false;

    }
    public void setBeginNumByPage(){
        this.beginNum = g.getNameToNodeIndex(Page4.getStart());
        this.endNum = g.getNameToNodeIndex(Page4.getEnd());
        setBeginNum = true;
    }
    public int getBeginNum() {
        return beginNum;
    }
    public int getEndNum() {
        return endNum;
    }
    public ArrayList<Integer> getWayToPoint() {
        return WayToPoint;
    }


    public void setBeginNum(int x){beginNum = x;}
    public void setEndNum(int x){endNum = x;}
    public void setStrategy(int x){
        if(x == 1) strategy = new Strategy1();
        else if(x == 2) strategy = new Strategy2();
        else if(x == 3) strategy = new Strategy3();
        else if(x == 4) strategy = new Strategy4();
    }
    public void setWaytoPoint(ArrayList<Integer> WayToPoint){
        this.WayToPoint = WayToPoint;
    }

    public boolean judgeBetweenCampus(){
        return g.getNode(beginNum) != g.getNode(endNum);
    }


    public int Dijkstra(int start, int end){
        PriorityQueue<Edge> pq = new PriorityQueue<>(strategy.getCmp());
        int[] dis = new int[Constants.MAXIMUM_Node];
        Edge[] fa = new Edge[Constants.MAXIMUM_Node];
        Edge[] buf = new Edge[Constants.MAXIMUM_Edge];
        cannotApproach = 0;
        for(int i = 1; i < Constants.MAXIMUM_Node; i++){
            dis[i] = Constants.inf;
        }
        dis[start] = 0;
        pq.add(new Edge(start, 0));
        while(!pq.isEmpty()){
            Edge u = pq.poll();
            if(u.getTo() == end)
                break;
            Vertex head = g.getHeadVertex(u.getTo());
            while(head.hasNext()){
                Edge v = head.getNextVertex().getEdge();
                int t = v.getTo();
                //TODO:没有增加策略不同的弹性
                if(dis[t] > dis[u.getTo()] + v.getLength()){
                    dis[t] = dis[u.getTo()] + v.getLength();
                    pq.add(new Edge(t, dis[t]));
                    fa[t] = v;
                }
                head = head.getNextVertex();
            }
        }
        if(dis[end] == Constants.inf) {
            Mylog.lWprintf("endpoint cannot approach");
            cannotApproach = 1;
            return -1;
        }else{
            int rec = end;
            int cnt = 0;
            while(rec != start){
                buf[cnt++] = fa[rec];
                rec = fa[rec].getFrom();
            }
            //get reverse route
            logger.info("get reverse route");
            for(int i = cnt - 1; i >= 0; i--){
                buffer.add(buf[i]);
            }
            //Correction
            logger.info("successfully get the direct route");
            return dis[end];
        }
    }
    public void addMustBy(int x) {
        WayToPoint.add(x);
    }
    public void confirmedStart(){
        if(WayToPoint.isEmpty()){
            int min_length = Dijkstra(beginNum, endNum);
            route.addAll(buffer);
            buffer.clear();
        }
        else{
            TSP TSP_method = new TSP(this);
            TSP_solution solution = TSP_method.SA_TSP();
            for(int i = 0; i <= WayToPoint.size(); i++){
                int min_length = Dijkstra(WayToPoint.get(solution.getInitNum(solution.path[i])), solution.getInitNum(solution.path[i + 1]));
                route.addAll(buffer);
                buffer.clear();
            }
        }
    }
    public void showRoute(){
        int num = buffer.size();
        if(cannotApproach == 1){
            System.out.println("Sorry, you can not approach from "
                    + g.getNodeIndexToName(beginNum)
                    + " -> "
                    + g.getNodeIndexToName(endNum)
            );
            return;
        }
        System.out.println("Great, You find the way!\n");
        StringBuilder info = new StringBuilder(g.getNodeIndexToName(beginNum));
        for (Edge edge : buffer) {
            info.append(" -> ").append(g.getNodeIndexToName(edge.getTo()));
        }
        System.out.println(info.toString());
    }
    public void go(){
        int dis = Dijkstra(beginNum, endNum);
        logger.debug("shortest distance:" + dis);
        showRoute();
        // TESTED
    }

    public int getRoute(int index) {
        return route.get(index).getTo();
    }
    public int getNum() {
        return route.size();
    }
    public Node getNode(int index){
        return g.getNode(index);
    }
}
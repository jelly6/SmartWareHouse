package com.example.smartwarehouse.viewDesign;

import android.util.Log;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

// queue node used in BFS
class Node
{
    // (x, y) represents matrix cell coordinates
    // dist represent its minimum distance from the source
    int x, y, dist;
    List<Pos> route = new ArrayList<>();

    Node(int x, int y, int dist,List<Pos> route) {
        this.x = x;
        this.y = y;
        this.dist = dist;
        this.route =route;
    }
    List<Pos> getRoute(){
        return route;
    }
}

public class MazeSolver {
    public static final String TAG = MazeSolver.class.getSimpleName();
    // M x N matrix
    private int M = 10;
    private int N = 10;

    // Below arrays details all 4 possible movements from a cell
    private static final int[] row = { -1, 0, 0, 1 };
    private static final int[] col = { 0, -1, 1, 0 };

    int[][] map;
    List<Pos> route;

    public MazeSolver(int[][] map,Pos start,Pos end) {
        //this.map = transpose(map);
        this.map =map;
        M=map.length;
        N=map[0].length;
        Log.d(TAG, "MazeSolver: "+M);
        Log.d(TAG, "MazeSolver: "+N);
        route =BFS(map,start.getY(),start.getX(),end.getY(),end.getX());
    }

    // Function to check if it is possible to go to position (row, col)
    // from current position. The function returns false if (row, col)
    // is not a valid position or has value 0 or it is already visited
    private boolean isValid(int[][] mat, boolean[][] visited, int row, int col)
    {
        return (row >= 0) && (row < M) && (col >= 0) && (col < N)
                && mat[row][col] == 1 && !visited[row][col];
    }
    public int[][] transpose(int[][] a) {
        int data[][] = new int[a.length][a[0].length];
        //System.out.println("轉置前：");
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j <a[0].length; j++) {
                data[i][j] = a[j][i];
            }
        }
        return data;
    }

    // Find Shortest Possible Route in a matrix mat from source
    // cell (i, j) to destination cell (x, y)
    private List<Pos> BFS(int[][] mat, int i, int j, int x, int y)
    {
        // construct a matrix to keep track of visited cells
        boolean[][] visited = new boolean[M][N];

        // create an empty queue
        Queue<Node> q = new ArrayDeque<>();

        // mark source cell as visited and enqueue the source node
        visited[i][j] = true;
        List<Pos> poss =new ArrayList<>();
        poss.add(new Pos(i,j));
        q.add(new Node(i, j, 0,poss));

        // stores length of longest path from source to destination
        int min_dist = Integer.MAX_VALUE;

        // loop till queue is empty
        while (!q.isEmpty())
        {
            // pop front node from queue and process it
            Node node = q.poll();

            // (i, j) represents current cell and dist stores its
            // minimum distance from the source
            i = node.x;
            j = node.y;
            int dist = node.dist;

            // if destination is found, update min_dist and stop
            if (i == x && j == y)
            {
                min_dist = dist;
                for (Pos pos : node.getRoute()) {
                    Log.d(TAG, "BFS: ("+pos.x+", "+pos.y+")");
                }
                return node.getRoute();
                //break;
            }

            // check for all 4 possible movements from current cell
            // and enqueue each valid movement
            for (int k = 0; k < 4; k++)
            {
                // check if it is possible to go to position
                // (i + row[k], j + col[k]) from current position
                if (isValid(mat, visited, i + row[k], j + col[k]))
                {
                    // mark next cell as visited and enqueue it
                    visited[i + row[k]][j + col[k]] = true;
                    List<Pos> route = new ArrayList<>();
                    route.addAll(node.getRoute());
                    route.add(new Pos(i + row[k], j + col[k]));
                    q.add(new Node(i + row[k], j + col[k], dist + 1, route));
                }
            }
        }

        if (min_dist != Integer.MAX_VALUE) {
            Log.d(TAG, "BFS: "+"The shortest path from source to destination " +
                    "has length " + min_dist);
            System.out.print("The shortest path from source to destination " +
                    "has length " + min_dist+"\n");

        }
        else {
            Log.d(TAG, "BFS: Destination can't be reached from given source");
            System.out.print("Destination can't be reached from given source");
        }
        return null;
    }

    public List<Pos> getRoute() {
        return route;
    }
    public List<Pos> getRouteFlip(){
        List<Pos> newRoute = new ArrayList<>();
        for (Pos pos : route) {
            Pos newP = new Pos(pos.getY(),pos.getX());
            newRoute.add(newP);
        }
        return newRoute;
    }
}

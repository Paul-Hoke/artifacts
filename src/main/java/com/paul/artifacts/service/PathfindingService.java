package com.paul.artifacts.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

@Slf4j
@Service
@RequiredArgsConstructor
public class PathfindingService {

  private static final int[][] DIRS = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

  private final OverworldMapCache mapCache;

  /**
   * BFS shortest path from (sx, sy) to (tx, ty) using the overworld tile graph.
   * Returns an ordered deque of [x, y] steps — the start is excluded, the destination
   * is included. Returns an empty deque if already at the destination or no path exists.
   */
  public Deque<int[]> findPath(int sx, int sy, int tx, int ty) {
    if (sx == tx && sy == ty) return new ArrayDeque<>();

    String startKey = sx + "," + sy;
    String goalKey  = tx + "," + ty;

    Map<String, String> prev = new HashMap<>();
    Queue<int[]> queue = new LinkedList<>();
    prev.put(startKey, null);
    queue.add(new int[]{sx, sy});

    while (!queue.isEmpty()) {
      int[] cur = queue.poll();
      String curKey = cur[0] + "," + cur[1];

      if (curKey.equals(goalKey)) {
        return reconstructPath(prev, startKey, goalKey);
      }

      for (int[] d : DIRS) {
        int nx = cur[0] + d[0];
        int ny = cur[1] + d[1];
        String nk = nx + "," + ny;
        if (!prev.containsKey(nk) && mapCache.isWalkable(nx, ny)) {
          prev.put(nk, curKey);
          queue.add(new int[]{nx, ny});
        }
      }
    }

    log.debug("No path found from ({},{}) to ({},{})", sx, sy, tx, ty);
    return new ArrayDeque<>();
  }

  private Deque<int[]> reconstructPath(Map<String, String> prev, String startKey, String goalKey) {
    LinkedList<int[]> path = new LinkedList<>();
    String k = goalKey;
    while (!k.equals(startKey)) {
      String[] parts = k.split(",");
      path.addFirst(new int[]{Integer.parseInt(parts[0]), Integer.parseInt(parts[1])});
      k = prev.get(k);
    }
    return new ArrayDeque<>(path);
  }
}

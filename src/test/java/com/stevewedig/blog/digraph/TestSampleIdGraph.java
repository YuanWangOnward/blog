package com.stevewedig.blog.digraph;

import static com.stevewedig.blog.translate.FormatLib.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.collect.*;
import com.stevewedig.blog.digraph.id_graph.*;
import com.stevewedig.blog.errors.NotThrown;

// example graph containing cycles (a->b->c->d->a, and a->e->a)
//
// id -> parentIds...
// a -> d, e
// b -> a
// c -> b
// d -> c
// e -> a
// f ->
//
// childIds <- id...
// b, e <- a
// c <- b
// d <- c
// a <- d
// a <- e
// <- f
public class TestSampleIdGraph {

  // ===========================================================================
  // ids
  // ===========================================================================

  private static ImmutableSet<String> idSet = parseSet("a, b, c, d, e, f");

  // ===========================================================================
  // graphs
  // ===========================================================================

  private static IdGraph<String> idGraphFromParentMap() {
    return IdGraphLib.fromParentMap(idSet, getParentMap());
  }

  private static Multimap<String, String> getParentMap() {
    Multimap<String, String> id__parentIds = HashMultimap.create();

    // a -> d, e
    // b -> a
    // c -> b
    // d -> c
    // e -> a
    // f ->
    id__parentIds.put("a", "d");
    id__parentIds.put("a", "e");
    id__parentIds.put("b", "a");
    id__parentIds.put("c", "b");
    id__parentIds.put("d", "c");
    id__parentIds.put("e", "a");

    return id__parentIds;
  }

  // ===================================

  private static IdGraph<String> idGraphFromChildMap() {
    return IdGraphLib.fromChildMap(idSet, getChildMap());
  }

  private static Multimap<String, String> getChildMap() {

    Multimap<String, String> id__childIds = HashMultimap.create();

    // b, e <- a
    // c <- b
    // d <- c
    // a <- d
    // a <- e
    // <- f
    id__childIds.put("a", "b");
    id__childIds.put("a", "e");
    id__childIds.put("b", "c");
    id__childIds.put("c", "d");
    id__childIds.put("d", "a");
    id__childIds.put("e", "a");

    return id__childIds;
  }

  // ===========================================================================
  // tests
  // ===========================================================================

  @Test
  public void testIdGraphFromParentMap() {
    verifyIdGraph(idGraphFromParentMap());
  }

  @Test
  public void testIdGraphFromChildMap() {

    verifyIdGraph(idGraphFromChildMap());
  }

  // ===========================================================================
  // verify graph
  // ===========================================================================

  public static void verifyIdGraph(IdGraph<String> graph) {

    // =================================
    // ids
    // =================================

    assertEquals(idSet, graph.idSet());

    assertEquals(idSet.size(), graph.idSize());

    graph.assertIdsEqual(idSet);

    try {
      graph.assertIdsEqual(parseSet("xxx"));
      throw new NotThrown(AssertionError.class);
    } catch (AssertionError e) {
    }

    // =================================
    // parents
    // =================================

    // a -> d, e
    // b -> a
    // c -> b
    // d -> c
    // e -> a
    // f ->

    assertEquals(getParentMap(), graph.id__parentIds());

    assertTrue(graph.parentOf("e", "a"));
    assertFalse(graph.parentOf("f", "a"));

    assertEquals(parseSet("d, e"), graph.parentIdSet("a"));
    assertEquals(parseSet("a"), graph.parentIdSet("b"));
    assertEquals(parseSet(""), graph.parentIdSet("f"));

    assertEquals(parseMultimap("a = e, e = a, a = d"), graph.filterParentMap(parseSet("a, e, d")));

    assertEquals(parseMultimap(""), graph.filterParentMap(parseSet("f")));

    // =================================
    // children
    // =================================

    // b, e <- a
    // c <- b
    // d <- c
    // a <- d
    // a <- e
    // <- f

    assertEquals(getChildMap(), graph.id__childIds());

    assertTrue(graph.childOf("e", "a"));
    assertFalse(graph.childOf("f", "a"));

    assertEquals(parseSet("b, e"), graph.childIdSet("a"));
    assertEquals(parseSet("c"), graph.childIdSet("b"));
    assertEquals(parseSet(""), graph.childIdSet("f"));

    // TODO filterChildMap

    // =================================
    // ancestors
    // =================================

    assertTrue(graph.ancestorOf("a", "b", true));
    assertTrue(graph.ancestorOf("a", "a", true));
    assertTrue(graph.ancestorOf("a", "b", false));
    assertFalse(graph.ancestorOf("a", "f", false));
    assertFalse(graph.ancestorOf("a", "a", false));

    assertEquals(parseSet("b, c, d, e"), graph.ancestorIdSet("a", false));
    assertEquals(parseSet("a, b, c, d, e"), graph.ancestorIdSet("a", true));

    assertEquals(parseSet(""), graph.ancestorIdSet("f", false));
    assertEquals(parseSet("f"), graph.ancestorIdSet("f", true));

    assertEquals(IdGraphLib.fromParentMap(parseSet("b, c, d, e"), "c", "b", "d", "c"),
        graph.ancestorIdGraph(parseSet("a"), false));

    assertEquals(IdGraphLib.fromParentMap(parseSet("f")),
        graph.ancestorIdGraph(parseSet("f"), true));

    // =================================
    // descendants
    // =================================

    assertEquals(parseSet("b, c, d, e"), graph.descendantIdSet("a", false));
    assertEquals(parseSet("a, c, d, e"), graph.descendantIdSet("b", false));
    assertEquals(parseSet("a, b, d, e"), graph.descendantIdSet("c", false));
    assertEquals(parseSet("a, b, c, e"), graph.descendantIdSet("d", false));
    assertEquals(parseSet("a, b, c, d"), graph.descendantIdSet("e", false));
    assertEquals(parseSet(""), graph.descendantIdSet("f", false));

    // =================================
    // roots (sources)
    // =================================

    assertEquals(parseSet("f"), graph.rootIdSet());

    // =================================
    // leaves (sinks)
    // =================================

    assertEquals(parseSet("f"), graph.leafIdSet());

    // =================================
    // topological sort
    // =================================

    assertTrue(graph.containsCycle());

    assertFalse(graph.optionalTopsortIdList().isPresent());

  }
}

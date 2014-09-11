package com.stevewedig.blog.digraph.id_graph;

import java.util.List;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;
import com.stevewedig.blog.util.LambdaLib.Fn1;

/**
 * A digraph containing ids.
 */
public interface IdGraph<Id> {

  // ===========================================================================
  // idSet
  // ===========================================================================

  /**
   * The set of ids.
   */
  ImmutableSet<Id> idSet();

  /**
   * The number of ids.
   */
  int idSize();

  // ===========================================================================
  // parents
  // ===========================================================================

  /**
   * The mapping from id to parent ids.
   */
  ImmutableSetMultimap<Id, Id> id__parentIds();

  /**
   * Getting an id's parent ids.
   */
  ImmutableSet<Id> parentIdSet(Id id);

  /**
   * Lambda that gets an id's parent ids.
   */
  Fn1<Id, List<Id>> parentIdListLambda();

  // ===========================================================================
  // children
  // ===========================================================================

  /**
   * The mapping from id to child ids.
   */
  ImmutableSetMultimap<Id, Id> id__childIds();

  /**
   * Getting an id's child ids.
   */
  ImmutableSet<Id> childIdSet(Id id);

  /**
   * Lambda that gets an id's child ids.
   */
  Fn1<Id, List<Id>> childIdListLambda();

  // ===========================================================================
  // ancestors
  // ===========================================================================

  /**
   * Getting an id's ancestor id iterable (its parents, it's parents' parents, and so on).
   */
  Iterable<Id> ancestorIdIterable(Id id);

  /**
   * Getting an id's ancestor id set (its parents, it's parents' parents, and so on).
   */
  ImmutableSet<Id> ancestorIdSet(Id id);

  // ===========================================================================
  // descendants
  // ===========================================================================

  /**
   * Getting an id's descendant id iterable (its children, it's childrens' children, and so on).
   */
  Iterable<Id> descendantIdIterable(Id id);

  /**
   * Getting an id's descendant id set (its children, it's childrens' children, and so on).
   */
  ImmutableSet<Id> descendantIdSet(Id id);

  // ===========================================================================
  // roots (sources)
  // ===========================================================================

  /**
   * The digraph's root (source) ids, so the ids without parents.
   */
  ImmutableSet<Id> rootIdSet();

  // ===========================================================================
  // leaves (sinks)
  // ===========================================================================

  /**
   * The digraph's leaf (sink) ids, so the ids without children.
   */
  ImmutableSet<Id> leafIdSet();

  // ===========================================================================
  // topological sort
  // ===========================================================================

  /**
   * Whether the digraph contains a cycle.
   */
  boolean containsCycle();

  /**
   * A topologically sorted list of ids, with roots (sources) first (will be absent if the digraph is cyclic).
   */
  Optional<ImmutableList<Id>> optionalTopsortIdList();

  // ===========================================================================
  // generic traversal
  // ===========================================================================

  /**
   * Generic id traversal as an iterable.
   * 
   * @param depthFirst Whether to traverse depth first or breadth first.
   * @param includeStarts Whether to include the start ids in the traversal.
   * @param startIds The initial id set.
   * @param expand A function mapping an id to the next ids.
   * @return An id iterable corresponding to the traversal.
   */
  Iterable<Id> idIterable(boolean depthFirst, boolean includeStarts, ImmutableList<Id> startIds,
      Fn1<Id, List<Id>> expand);

  /**
   * Generic id traversal copied into a list.
   * 
   * @param depthFirst Whether to traverse depth first or breadth first.
   * @param includeStarts Whether to include the start ids in the traversal.
   * @param startIds The initial id set.
   * @param expand A function mapping an id to the next ids.
   * @return An id list corresponding to the traversal.
   */
  ImmutableList<Id> idList(boolean depthFirst, boolean includeStarts, ImmutableList<Id> startIds,
      Fn1<Id, List<Id>> expand);

}
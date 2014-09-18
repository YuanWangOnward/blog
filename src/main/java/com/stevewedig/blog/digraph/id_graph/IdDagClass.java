package com.stevewedig.blog.digraph.id_graph;

import java.util.Set;

import com.google.common.collect.*;
import com.stevewedig.blog.digraph.errors.DagCannotHaveCycle;

/**
 * An implementation of IdDag.
 */
public class IdDagClass<Id> extends IdGraphClass<Id> implements IdDag<Id> {

  // ===========================================================================
  // constructor
  // ===========================================================================

  public IdDagClass(ImmutableSet<Id> idSet, ImmutableSetMultimap<Id, Id> id__parentIds) {

    super(idSet, id__parentIds);

    validate();
  }

  // ===========================================================================
  // validate
  // ===========================================================================

  private void validate() throws DagCannotHaveCycle {

    if (containsCycle())
      throw new DagCannotHaveCycle();
  }

  // ===========================================================================
  // ancestors
  // ===========================================================================

  @Override
  public IdDag<Id> ancestorIdGraph(Id id, boolean inclusive) {
    return ancestorIdGraph(ImmutableSet.of(id), inclusive);
  }

  @Override
  public IdDag<Id> ancestorIdGraph(Set<Id> ids, boolean inclusive) {

    ImmutableSet<Id> ancestorIds = ancestorIdSet(ids, inclusive);

    return IdDagLib.fromParentMap(ancestorIds, filterParentMap(ancestorIds));
  }

  // ===========================================================================
  // descendants
  // ===========================================================================

  @Override
  public IdDag<Id> descendantIdGraph(Id id, boolean inclusive) {
    return descendantIdGraph(ImmutableSet.of(id), inclusive);
  }

  @Override
  public IdDag<Id> descendantIdGraph(Set<Id> ids, boolean inclusive) {

    ImmutableSet<Id> descendantIds = descendantIdSet(ids, inclusive);

    return IdDagLib.fromParentMap(descendantIds, filterParentMap(descendantIds));
  }
  
  // ===========================================================================
  // toplogical sort
  // ===========================================================================

  @Override
  public ImmutableList<Id> topsortIdList() {
    return optionalTopsortIdList().get();
  }

  // ===========================================================================
  // depth first
  // ===========================================================================

  @Override
  public Iterable<Id> depthIdIterable() {
    return idIterable(true, true, ImmutableList.copyOf(rootIdSet()), childIdListLambda());
  }

  // ===================================

  @Override
  public ImmutableList<Id> depthIdList() {
    if (depthIdList == null)
      depthIdList = ImmutableList.copyOf(depthIdIterable());
    return depthIdList;
  }

  private ImmutableList<Id> depthIdList;

  // ===========================================================================
  // breadth first
  // ===========================================================================

  @Override
  public Iterable<Id> breadthIdIterable() {
    return idIterable(false, true, ImmutableList.copyOf(rootIdSet()), childIdListLambda());
  }

  // ===================================

  @Override
  public ImmutableList<Id> breadthIdList() {
    if (breadthIdList == null)
      breadthIdList = ImmutableList.copyOf(breadthIdIterable());
    return breadthIdList;
  }

  private ImmutableList<Id> breadthIdList;

}

package com.stevewedig.blog.digraph.node;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;
import com.stevewedig.blog.digraph.id_graph.IdDag;
import com.stevewedig.blog.digraph.id_graph.IdDagLib;
import com.stevewedig.blog.digraph.id_graph.IdGraph;
import com.stevewedig.blog.digraph.id_graph.IdGraphLib;
import com.stevewedig.blog.digraph.id_graph.IdTree;
import com.stevewedig.blog.digraph.id_graph.IdTreeLib;

/**
 * A library with tools for manipulating UpNodes.
 */
public abstract class UpNodeLib {

  // ===========================================================================
  // creating nodes
  // ===========================================================================

  public static <Id> UpNode<Id> upNode(Id id, Iterable<Id> parentIds) {
    return new UpNodeClass<Id>(id, ImmutableSet.copyOf(parentIds));
  }

  @SafeVarargs
  public static <Id> UpNode<Id> upNode(Id id, Id... parentIds) {
    return upNode(id, ImmutableSet.copyOf(parentIds));
  }

  // ===========================================================================
  // nodeIter -> idSet
  // ===========================================================================

  public static <Id, Node extends UpNode<Id>> ImmutableSet<Id> nodes__ids(Iterable<Node> nodes) {

    ImmutableSet.Builder<Id> ids = ImmutableSet.builder();

    for (Node node : nodes) {
      ids.add(node.id());
      ids.addAll(node.parentIds());
    }

    return ids.build();
  }

  // ===========================================================================
  // nodeIter -> parentMap
  // ===========================================================================

  public static <Id, Node extends UpNode<Id>> ImmutableSetMultimap<Id, Id> nodes__parentMap(
      Iterable<Node> nodes) {

    ImmutableSetMultimap.Builder<Id, Id> id__parentIds = ImmutableSetMultimap.builder();

    for (Node node : nodes) {
      id__parentIds.putAll(node.id(), node.parentIds());
    }

    return id__parentIds.build();
  }

  // ===========================================================================
  // nodeSet -> nodeMap
  // ===========================================================================

  public static <Id, Node extends UpNode<Id>> ImmutableBiMap<Id, Node> nodes__nodeMap(
      ImmutableSet<Node> nodes) {

    BiMap<Id, Node> id__node = HashBiMap.create();

    for (Node node : nodes) {

      if (id__node.containsKey(node.id()))
        throw new NodeIdConflict("id = %s", node.id());

      id__node.put(node.id(), node);
    }

    return ImmutableBiMap.copyOf(id__node);
  }

  // ===========================================================================
  // nodeSet -> idGraph, idDag, idTree
  // ===========================================================================

  public static <Id, Node extends UpNode<Id>> IdGraph<Id> nodes__idGraph(Iterable<Node> nodes) {

    ImmutableSet<Id> ids = nodes__ids(nodes);

    ImmutableSetMultimap<Id, Id> id__parentIds = nodes__parentMap(nodes);

    return IdGraphLib.fromParentMap(ids, id__parentIds);
  }

  public static <Id, Node extends UpNode<Id>> IdDag<Id> nodes__idDag(ImmutableSet<Node> nodes) {

    ImmutableSet<Id> ids = nodes__ids(nodes);

    ImmutableSetMultimap<Id, Id> id__parentIds = nodes__parentMap(nodes);

    return IdDagLib.fromParentMap(ids, id__parentIds);
  }

  public static <Id, Node extends UpNode<Id>> IdTree<Id> nodes__idTree(ImmutableSet<Node> nodes) {

    ImmutableSet<Id> ids = nodes__ids(nodes);

    ImmutableSetMultimap<Id, Id> id__parentIds = nodes__parentMap(nodes);

    return IdTreeLib.fromParentMap(ids, id__parentIds);
  }

}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package coq;

import edu.uci.ics.jung.algorithms.filters.VertexPredicateFilter;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.shortestpath.BFSDistanceLabeler;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFrame;
import org.apache.commons.collections15.Predicate;
import org.apache.commons.collections15.Transformer;
import org.jgrapht.alg.StrongConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

/**
 *
 * @author Abhishek
 */
public class DebugUnivInconst {
    JFrame topUnivs=null;

    /**
     * 
     * @param constraints : the string containing the output of "Print Universes."
     * @return a list of constraint objects
     */
    static ArrayList<Constraint> parseConstraints(String constraints)
    {
            String[] lines=constraints.split("\n");
            String curLHS="";
            ArrayList<Constraint> ret= new ArrayList<Constraint>();
      for (String line : lines) {
        if(line.trim().isEmpty())
          continue;
        
        Constraint constr=new Constraint(line);
        
        if(constr.lhs.isEmpty())
          constr.lhs=curLHS;
        else
          curLHS=constr.lhs;
        
        ret.add(constr);
      }   
            return ret;
    }
    
    void showTopUnivs(String constraints)
    {
      ArrayList<Constraint> cts= parseConstraints(constraints);
      DirectedSparseMultigraph<String,String> g= new DirectedSparseMultigraph<String, String>();
      for (int i=0;i<cts.size();i++)
      {
        Constraint constr=cts.get(i);
        if(constr.involvesTop())
           constr.addToGraph(g, i+1);
      }
            
      showGraph(makeEqualitiesUndirected(g),true, "Univ. levels in Top module",new HashSet<String>());
        
    }

    static class EdgeColor implements Transformer<String, Paint>
    {

      @Override
      public Paint transform(String edgeLabel) {
          if (edgeLabel.startsWith("s")) // <=
              return Color.RED;
          else if(edgeLabel.startsWith("n")) // <
              return Color.GREEN;
          else
              return Color.BLUE; // =
      }
    }
    
    static class FilterNodesOut implements Predicate<String>
    {
      Set<String> filterOut;

      public FilterNodesOut(Set<String> filterOut) {
        this.filterOut = filterOut;
      }
      
      @Override
      public boolean evaluate(String t) {
        return !(filterOut.contains(t));
      }
    }

    static class FilterKeepNodes implements Predicate<String>
    {
      Set<String> keepNodes;

      public FilterKeepNodes(Set<String> keepNodes) {
        this.keepNodes = keepNodes;
      }
      
      @Override
      public boolean evaluate(String t) {
        return (keepNodes.contains(t));
      }
    }

    static class FilterLeavesOut implements Predicate<String>
    {
      Graph<String,Object> graph;

      public FilterLeavesOut(Graph graph) {
        this.graph = graph;
      }
      
      @Override
      public boolean evaluate(String t) {
        return (graph.getNeighborCount(t)>1);
         // if a variable(vertex) has only 1 constraint, it can be trivially solved
      }
    }

    static class VertexColor implements Transformer<String, Paint>
    {
      HashSet<String> highlightNodes;

    public VertexColor(HashSet<String> highlightNodes) {
      this.highlightNodes = highlightNodes;
    }

      
      
      @Override
      public Paint transform(String node) {
        
        if(highlightNodes.contains(node))
          return Color.CYAN;
        else
          return Color.GREEN;
      }
      
    }
  
    void showGraph(Graph g,boolean newWindow, String title, HashSet<String>  highlightNodes)
    {
            FRLayout<String, String> layout=new FRLayout<String, String>(g);
            System.err.println("----- "+title+"------");
            System.out.println("#edges: "+g.getEdgeCount());
            System.out.println("#nodes: "+g.getVertexCount());
            System.err.println("-----------------------");
            int numV=g.getVertexCount();
            layout.setSize(new Dimension(Math.max(600, numV), Math.max(600, numV)));
            VisualizationViewer<String, String> vv=
                        new VisualizationViewer<String, String>(layout);
            DefaultModalGraphMouse gm = new DefaultModalGraphMouse();
            gm.setMode(ModalGraphMouse.Mode.PICKING);
            vv.setGraphMouse(gm);
            vv.getRenderContext().setVertexDrawPaintTransformer(new VertexColor(highlightNodes));
            vv.getRenderContext().setVertexFillPaintTransformer(new VertexColor(highlightNodes));
            vv.getRenderContext().setArrowDrawPaintTransformer(new EdgeColor());
            vv.getRenderContext().setArrowFillPaintTransformer(new EdgeColor());
            vv.getRenderContext().setEdgeDrawPaintTransformer(new EdgeColor());
            vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<String>());
            vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
            
            
            JFrame jd;
            if(newWindow || topUnivs==null)
            {
                jd=new JFrame();
                jd.setTitle(title);
                jd.setSize(new Dimension(1000, 800));
               // jd.pack();
            }
            else
            {
                jd=topUnivs;                
            }
            
            if(topUnivs==null&&!newWindow)
                topUnivs=jd;
            
            jd.getContentPane().removeAll();
            jd.getContentPane().add(vv);
            jd.setVisible(true);
      
    }
    static class Constraint
    {
         public String lhs, rhs, edgetype;

        public static String LESS_THAN="s";
        public Constraint(String lhs, String rhs, String line) {
          this.lhs = lhs;
          this.rhs = rhs;
          if(line.contains("<="))
            edgetype="n";
          else if (line.contains("<"))
            edgetype=LESS_THAN;
          else
            edgetype="e";
        }

        boolean involvesTop()
        {
            return lhs.startsWith("Top.") || rhs.startsWith("Top.");
        }
        
        public Constraint(String line) {
                String frags[]=line.split("[<=]");
                if(frags.length==3)
                {
                    edgetype="n"; // not strict inequality (<=)
                    lhs=frags[0].trim();
                    assert(frags[1].isEmpty());
                    rhs=frags[2].trim();
                } 
                else
                {
                    assert(frags.length==2);
                
                    lhs=frags[0].trim();
                    rhs=frags[1].trim();                    
                    if (line.contains("<"))
                        edgetype = LESS_THAN; // strict inequality (<)
                    else
                    {
                        assert(line.contains("="));
                        edgetype = "e"; // equality (=)
                    }
                }
        }
        
        public void addHelpfulNames(HashMap<String, String> help)
        {
            if(help==null|| help.isEmpty())
                return;
            
            if(help.containsKey(lhs))
                lhs=help.get(lhs);
            
            if(help.containsKey(rhs))
                rhs=help.get(rhs);
            
        }
        public void addToGraph(Graph<String,String> g, int i)
        {
              String edgelabel=edgetype+i;
              assert(!lhs.isEmpty());
              assert(!rhs.isEmpty());
              g.addVertex(lhs);
              g.addVertex(rhs);
              //EdgeType edt=EdgeType.DIRECTED;
              
              //if(edgetype.equals("e"))
                //edt=EdgeType.UNDIRECTED;
              boolean bl=g.addEdge(edgelabel, rhs, lhs, EdgeType.DIRECTED );
              assert(bl);// arrow can be read as <,i.e for a<b, arrow is at a.                

        }
        
        boolean isStrict()
        {
          return edgetype.equals(LESS_THAN);
        }
        
       static ArrayList<Constraint> keepStrictEdges(ArrayList<Constraint> constraints)
       {
         ArrayList<Constraint> ret= new ArrayList<Constraint>();
         for(Constraint constr: constraints)
         {
           if(constr.isStrict())
             ret.add(constr);
         }
         
         return ret;
       }
    }
    
    
    Set<String> getVerticesToDiscard(Graph<String,String> g, String lhs, String rhs)
    {
      // wrong because equalities need to be bidirectional
        BFSDistanceLabeler<String,String> bfs=new BFSDistanceLabeler<String, String>();
        HashSet<String> roots=new HashSet<String>(2);
        roots.add(lhs);
        roots.add(rhs);
        bfs.labelDistances(g, roots);
        return bfs.getUnvisitedVertices();      
    }
    
    /**
     * 
     * @param s1
     * @param s2
     * @return 
     */
    static boolean nonDisjoint(HashSet<String> highlight, Set<String> scc)
    {
      if(highlight.isEmpty())
        return true;
      
      HashSet<String> highCopy= new HashSet<String> (highlight);
      highCopy.retainAll(scc);
      return (highCopy.size()>0);
    }
    
    static ArrayList<Set<String>> keepBadSCCs(List<Set<String>> sccs, ArrayList<Constraint> strictConstraints,HashSet<String> highlight)
    {
      ArrayList<Set<String>> ret= new ArrayList<Set<String>>();
      for(Set<String> scc: sccs)
        if(containsAStrictInequality(scc, strictConstraints) && nonDisjoint(highlight, scc))
          ret.add(scc);
      
      return ret;
    }

    static boolean containsAStrictInequality(Set<String> scc, ArrayList<Constraint> strictConstraints)
    {
      for(Constraint constr: strictConstraints)
        if(scc.contains(constr.lhs) && scc.contains(constr.rhs))
          return true;
                  
      return false;
    }

    ArrayList<Set<String>> getVerticesToKeepJgraph(DirectedSparseMultigraph<String,String> g,
        HashSet<String> highlightHodes, ArrayList<Constraint> strictConstraints)
    {
        SimpleDirectedGraph<String,String> jg =new SimpleDirectedGraph(DefaultEdge.class);
        Collection<String> edges = g.getEdges();
        for (String ed : edges)
        {
            String srcv = g.getSource(ed);
            String destv = g.getDest(ed);
            jg.addVertex(srcv);
            jg.addVertex(destv);
            jg.addEdge(srcv, destv,ed);
            if(ed.startsWith("e"))
               jg.addEdge(destv, srcv,ed);
        }
        
        StrongConnectivityInspector<String,String> insp=new StrongConnectivityInspector(jg);
        List<Set<String>> stronglyConnectedSets = insp.stronglyConnectedSets();
        //dbugcontents=dbugcontents+"\n\n\n found #SCCs"+stronglyConnectedSets+"\n\n";

        ArrayList<Set<String>> badSCCs= keepBadSCCs(stronglyConnectedSets, strictConstraints, highlightHodes);
        return badSCCs;
    }
    
    SparseMultigraph<String, String> makeEqualitiesUndirected(DirectedSparseMultigraph<String,String> g)
    { 
        SparseMultigraph<String, String> ret=new SparseMultigraph<String, String>();
        Collection<String> edges = g.getEdges();
        for (String ed : edges)
        {
            String srcv = g.getSource(ed);
            String destv = g.getDest(ed);
            ret.addVertex(srcv);
            ret.addVertex(destv);
            EdgeType et= EdgeType.DIRECTED;
            if(ed.startsWith("e"))
               et=EdgeType.UNDIRECTED;
            ret.addEdge(ed,srcv, destv,et);
        }
        
        return ret;
    }
    
    DirectedSparseMultigraph<String,String> filterKeepAndVisualize(DirectedSparseMultigraph<String, String> g, 
            Set<String> keepV, HashSet<String> highlightNodes, String windowTitle)
    {
        //dbugcontents=dbugcontents+"\n init # nodes:"+g.getVertexCount()+" # edges:"+ g.getEdgeCount();
        //dbugcontents=dbugcontents+"nodes left in SCC(#nodes):"+keepV.size();
        //init # nodes:1060 # edges:2911filtering removed(#nodes):943
        VertexPredicateFilter<String,String> vf =
                //new VertexPredicateFilter<String, String>(new FilterNodesOut(discardv));
        new VertexPredicateFilter<String, String>(new FilterKeepNodes(keepV));
        DirectedSparseMultigraph<String,String> filtered=(DirectedSparseMultigraph<String,String>) vf.transform(g);
        //uiWindow.enableCompileButtonsAndShowDbug();
        Collection<String> edges = filtered.getEdges();
        int count=0;
        for(String s:edges)
        {
            count=count+1;
           //dbugcontents=dbugcontents+s+"\n";
        }
        //dbugcontents=dbugcontents+"\n\n"+count+" both edges: \n\n";

        count=0;
        edges = g.getEdges();
        for(String s:edges)
        {
            count=count+1;
            if(s.startsWith("s")&&(keepV.contains(g.getSource(s)) || keepV.contains(g.getDest(s))))
            {
              //dbugcontents=dbugcontents+s+"\n";
              filtered.addEdge(s, g.getSource(s), g.getDest(s));
            }
        }
        //dbugcontents=dbugcontents+"\n\n"+count+" finish s edges: \n\n";
        
        //uiWindow.enableCompileButtonsAndShowDbug();
        
        showGraph(makeEqualitiesUndirected(filtered),true, windowTitle, highlightNodes);
        return filtered;
    }
    HashMap<String, String> helpfulConstrNames=null;
    
    void parseNames(String input)
    {
        if(helpfulConstrNames==null)
            helpfulConstrNames=new HashMap<String, String>();
        helpfulConstrNames.clear();
        
        String[] lines=input.split("\n");
        for(String line: lines)
        {
            String [] frags=line.split(":");
            helpfulConstrNames.put(frags[0],frags[1]);
            System.out.println("mapping " + frags[0] +" to "+frags[1]);
        }
    }
  
    int debugUnivInconsistency(ArrayList<Constraint> cts, HashSet<String> highlightNodes)
    {
            System.out.println("#constraints:" + cts.size());

            DirectedSparseMultigraph<String,String> g= new DirectedSparseMultigraph<String, String>();
            for(int i=0;i<cts.size();i++)
            {
                Constraint constr=cts.get(i);
                constr.addHelpfulNames(helpfulConstrNames);
                constr.addToGraph(g, i+1);
            }
          showGraph(makeEqualitiesUndirected(g),true, "Original Graph", highlightNodes);
          
          
          ArrayList<Set<String>> sccs = getVerticesToKeepJgraph(g,highlightNodes, Constraint.keepStrictEdges(cts));
          
          for(int i=0; i< sccs.size();i++)
          {
            filterKeepAndVisualize(g, sccs.get(i), highlightNodes,"SCC #"+i);
          }
          
          return sccs.size();
    }

  static String readFile(String path) 
  throws IOException 
  {
  byte[] encoded = Files.readAllBytes(Paths.get(path));
  return new String(encoded, Charset.defaultCharset());
  }  
    public static void main(String [ ] args) throws IOException
    {
      System.out.println("args"+Arrays.toString(args));
      if(args.length!=1)
      {
        System.err.println("usage : java -jar DebugUniv.jar filename\n where filename contains the constraints, e.g. output of Print Universes");
        return;
      }
      DebugUnivInconst dbg= new DebugUnivInconst();
      String fileString=readFile(args[0]);
      dbg.showTopUnivs(fileString);
      dbg.debugUnivInconsistency(parseConstraints(fileString), new HashSet<String>());
    }
}

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

    void showTopUnivs(String constraints)
    {
            DirectedSparseMultigraph<String,String> g= new DirectedSparseMultigraph<String, String>();
            
            String[] lines=constraints.split("\n");
            String curLHS="";
            for(int i=0;i<lines.length;i++)
            {
                String line=lines[i];
                if(line.trim().isEmpty())
                    continue;

                Constraint constr=new Constraint(line);
                
                if(constr.lhs.isEmpty())
                    constr.lhs=curLHS;
                else
                    curLHS=constr.lhs;
                
                constr.addHelpfulNames(helpfulConstrNames);
                if(constr.involvesTop())
                    constr.addToGraph(g, i+1);
            }
            showGraph(makeEqualitiesUndirected(g),"","",false, "Inconsistency");
        
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
      String lhs, rhs;

      public VertexColor(String lhs, String rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
      }
      
      @Override
      public Paint transform(String i) {
        if(i.equals(lhs))
          return Color.CYAN;
        else if(i.equals(rhs))
          return Color.MAGENTA;
        else
          return Color.GREEN;
      }
      
    }
  
    void showGraph(Graph g, String lhs, String rhs, boolean newWindow, String title)
    {
            FRLayout<String, String> layout=new FRLayout<String, String>(g);
            int numV=g.getVertexCount();
            layout.setSize(new Dimension(Math.max(600, numV), Math.max(600, numV)));
            VisualizationViewer<String, String> vv=
                        new VisualizationViewer<String, String>(layout);
            DefaultModalGraphMouse gm = new DefaultModalGraphMouse();
            gm.setMode(ModalGraphMouse.Mode.PICKING);
            vv.setGraphMouse(gm);
            vv.getRenderContext().setVertexDrawPaintTransformer(new VertexColor(lhs, rhs));
            vv.getRenderContext().setVertexFillPaintTransformer(new VertexColor(lhs, rhs));
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

        public Constraint(String lhs, String rhs, String line) {
          this.lhs = lhs;
          this.rhs = rhs;
          if(line.contains("<="))
            edgetype="n";
          else if (line.contains("<"))
            edgetype="s";
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
                        edgetype = "s"; // strict inequality (<)
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
              
              assert(g.addEdge(edgelabel, rhs, lhs, EdgeType.DIRECTED ));// arrow can be read as <,i.e for a<b, arrow is at a.                

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
    
    Set<String> getVerticesToKeepJgraph(DirectedSparseMultigraph<String,String> g,String vlhs, String vrhs)
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
        dbugcontents=dbugcontents+"\n\n\n found #SCCs"+stronglyConnectedSets+"\n\n";

        for (Set<String> scc :stronglyConnectedSets)
        {
            if (scc.contains(vlhs) || scc.contains(vrhs))
               return scc;
        }
        
        return null;
        
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
    
    DirectedSparseMultigraph<String,String> filterKeepAndVisualize(DirectedSparseMultigraph<String, String> g,Set<String> keepV,Constraint violatedConstr)
    {
        dbugcontents=dbugcontents+"\n init # nodes:"+g.getVertexCount()+" # edges:"+ g.getEdgeCount();
        dbugcontents=dbugcontents+"nodes left in SCC(#nodes):"+keepV.size();
        //init # nodes:1060 # edges:2911filtering removed(#nodes):943
        VertexPredicateFilter<String,String> vf =
                //new VertexPredicateFilter<String, String>(new FilterNodesOut(discardv));
        new VertexPredicateFilter<String, String>(new FilterKeepNodes(keepV));
        DirectedSparseMultigraph<String,String> filtered=(DirectedSparseMultigraph<String,String>) vf.transform(g);
        uiWindow.enableCompileButtonsAndShowDbug();
        Collection<String> edges = filtered.getEdges();
        int count=0;
        for(String s:edges)
        {
            count=count+1;
           dbugcontents=dbugcontents+s+"\n";
        }
        dbugcontents=dbugcontents+"\n\n"+count+" both edges: \n\n";

        count=0;
        edges = g.getEdges();
        for(String s:edges)
        {
            count=count+1;
            if(s.startsWith("s")&&(keepV.contains(g.getSource(s)) || keepV.contains(g.getDest(s))))
            {
              dbugcontents=dbugcontents+s+"\n";
              filtered.addEdge(s, g.getSource(s), g.getDest(s));
            }
        }
        dbugcontents=dbugcontents+"\n\n"+count+" finish s edges: \n\n";
        
        uiWindow.enableCompileButtonsAndShowDbug();
        
        showGraph(makeEqualitiesUndirected(filtered),violatedConstr.lhs,violatedConstr.rhs,true, "filtered SCC");
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
  
    void debugUnivInconsistency()
    {
        Pattern pat = Pattern.compile("\\(cannot enforce ([\\w_.]*)[ \\n]<[=]?[ \\n]([\\w_.]*)\\)");
        Matcher mat = pat.matcher(dbugcontents);
        Constraint violatedConstr;
        
        if(mat.find())
        {
            String toParse=mat.group().substring("(cannot enforce".length());
            violatedConstr=new Constraint(mat.group(1),mat.group(2),mat.group());
            violatedConstr.addHelpfulNames(helpfulConstrNames);

        }
        else
          return;
        
        CoqTopXMLIO.CoqRecMesg rec= getCoqtop().query("Print Universes.");        
        if(rec.success)
        {
            String constraints= rec.conciseReply;
            setDbugcontents(constraints);
            // strict equality of edge is true
            DirectedSparseMultigraph<String,String> g= new DirectedSparseMultigraph<String, String>();
            violatedConstr.addToGraph(g, 0);
        //    g.addVertex(start);
        //    g.addVertex(end);
        //    g.addEdge(strict,start, end, EdgeType.DIRECTED);
            
            String[] lines=constraints.split("\n");
            String curLHS="";
            for(int i=0;i<lines.length;i++)
            {
                String line=lines[i];
                if(line.trim().isEmpty())
                    continue;

                Constraint constr=new Constraint(line);
                
                if(constr.lhs.isEmpty())
                    constr.lhs=curLHS;
                else
                    curLHS=constr.lhs;
                
                constr.addHelpfulNames(helpfulConstrNames);
                constr.addToGraph(g, i+1);
            }
            showGraph(g,violatedConstr.lhs,violatedConstr.rhs,true, "Inconsistency");
          Set<String> keepV = getVerticesToKeepJgraph(g,violatedConstr.lhs,violatedConstr.rhs);
          filterKeepAndVisualize(g, keepV, violatedConstr);
          
          
            // filtering begins
            //Set<String> discardv=getVerticesToDiscard(g, violatedConstr.lhs, violatedConstr.rhs);
            
        }
        else
        {
            setDbugcontents(rec.toString());
        }
        
        
    }
  
}

package onepassassembler;

import java.util.ArrayList;
import java.util.Arrays;
//import java.util.Scanner;

public class OnePassAssembler {
//    static String arr[][];
    static String arr[][] = {
        {"","START","100",""},
        {"","MOVER","AREG","X"},
        {"L1","ADD","BREG","ONE"},
        {"","COMP","BREG","TEN"},
        {"","BC","EQ","LAST"},
        {"","ADD","AREG","ONE"},
        {"","BC","ANY","L1"},
        {"LAST","STOP","",""},
        {"X","DC","5",""},
        {"ONE","DC","1",""},
        {"TEN","DC","10",""},
        {"","END","",""}
        };

    static ArrayList<ArrayList<String>> machineInsTable;
    static ArrayList<ArrayList<String>>bt;
    static ArrayList<ArrayList<String>>blist;
    
    static void inputProcessing(String arr[][]){
        boolean stopped=false,exists=false;
        OpcodeTable obj=new OpcodeTable();     
        int opcode=0,inscode=0;
        machineInsTable=new ArrayList<>();//machine instruction table
        bt=new ArrayList<>();//backpatch table
        ArrayList<String>sub;//auxilliary sublist
        ArrayList<String>rs;

        blist=new ArrayList<>();//backpatch list
        
        int pc=Integer.parseInt(arr[0][2]);
        
        
        for(int i=0;i<arr.length-1;i++){
            sub=new ArrayList<>();
            sub.addAll(Arrays.asList(arr[i]));
            if(i==0){
               sub.add("--");
               machineInsTable.add(sub);
                continue;   
            }
            
            sub.add(pc+"");
            machineInsTable.add(sub);
            sub=new ArrayList<>();
            if(arr[i][1].equals("STOP")){
                stopped=true;
                sub.add("00");
                sub.add("00");
                sub.add("00");
                sub.add(i+"");
            }
            else if(arr[i][1].equals("DC")){
                sub.add("00");
                sub.add("00");
                sub.add( Integer.parseInt(arr[i][2]) < 10 ? "0"+arr[i][2]:arr[i][2]);
                sub.add(i+"");
            }else{
                
                opcode = obj.getCode(arr[i][1]);
                inscode= obj.getCode(arr[i][2]);
                sub.add("0"+opcode);
                sub.add(inscode==-1?"-":"0"+inscode);
                sub.add("--");
                sub.add(i+"");
            }
            bt.add(sub);
            
            if(!stopped){
                sub=new ArrayList<>();
                
                if(!arr[i][3].equals("")){ 
                    exists=false;
                    //to check if symbols already known
                    for(int k=0;k<machineInsTable.size();k++){
                        sub=machineInsTable.get(k);
                        if(arr[i][3].equals(sub.get(0))){
                            exists=true;
                            rs=bt.get(i-1);
                            rs.set(2,sub.get(4)+"");
                            bt.set(i-1, rs);
                        }
                    }
                    //if the symbols are new then adding them to backpatch list 
                    if(!exists){
                        sub=new ArrayList<>();
                        sub.add(pc+"");
                        sub.add(arr[i][3]);
                        sub.add(i+"");
                        blist.add(sub);
                    }   
                }
            }
                if(!arr[i][0].equals("")){
                    sub=new ArrayList<>();
                    for(int j=0;j<blist.size();j++){
                        sub=blist.get(j);
                        if(sub.get(1).equals(arr[i][0])){
                            int ind=Integer.parseInt(sub.get(2));
                            rs=bt.get(ind-1);
                            rs.set(2,pc+"");
                            bt.set(ind-1, rs);
//                            blist.remove(j);
                        }
                    }
                    
                }
             pc++;
        }
    }
    
    static void getMachineInstructionTable(){
        System.out.println("STEP:1 ---Machine Instruction Table with Program Counter---");
        System.out.println("Label\tOPCODE\t  OPERANDS \t P.C.");
        ArrayList<String>sub;
        for(int i=0;i<machineInsTable.size();i++)
        {
            sub=machineInsTable.get(i);
            System.out.println(sub.get(0)+"\t"+sub.get(1)+"\t"+sub.get(2)+"\t"+sub.get(3)+"\t"+sub.get(4));
        }
        System.out.println("--------------------------------------\n");

    }
    
    static void getBackpatchList(){
        
        ArrayList<String>sb;
        System.out.print("STEP 3: ---Backpatch List---\n");
        System.out.println("Address\t\tReference");
        for(int i=0;i<blist.size();i++){
            sb=blist.get(i);
            System.out.println(sb.get(0) +"\t\t"+sb.get(1));
        }
    }
    
    static void getBackpatchTable(){
        
        ArrayList<String> sb;
        System.out.print("STEP:2 ---Backpatch Table ---\n");
        System.out.print("OPCODE \tINS\tADDRESS\n");
        for(int i=0;i<bt.size();i++){
            sb=bt.get(i);
            System.out.println(sb.get(0)+"\t"+sb.get(1)+"\t"+sb.get(2));
        }
          System.out.println("--------------------------------------\n");
    }
    public static void main(String[] args) {
        inputProcessing(arr);//obtaining program Counter
        getMachineInstructionTable();//step 1
        getBackpatchTable();//step 2
        getBackpatchList();
         
    }
    
}
//       Scanner sc = new Scanner(System.in);
//       ArrayList<ArrayList<String>>ls = new ArrayList<>(); 
//       String s = "";
//         do{
//            s=sc.nextLine();
//            arr=s.split(" ");
//            ArrayList<String>str=new ArrayList<>();
//            str.addAll(Arrays.asList(arr));
//            ls.add(str);
//       }while(s.equals("end")==false);
        
//         System.out.println(ls);
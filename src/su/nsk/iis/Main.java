package su.nsk.iis;

import su.nsk.iis.cpn.ml.*;

public class Main {

    public static void main(String[] args) {
        try {
            //Parser.getParsedExpression("1+2");
            //Parser.getParsedExpression("1 div 2");
            //Parser.getParsedExpression("( ()@1) @+1");

            Parser.getParsedExpression("0::[~1 + 2 * 3, 4]");

            if (true) return;


            parse("colset allenums=with e | enum0 | RedLight | GreenLight | ApproachTimer | StartStopTimer | GetSemaphoreStateToTrain | TrainApproaching | CarApproaching | TrainCrossedGate | \n" +
                    "  StartCycleSignal | ApproachingToSemaphore | Stopped | ApproachingToGate | GateOpened | GateClosed | Awaiting | StartCycle | WaitCycleEnd;\n" +
                    "colset allsigs=allenums;\n" +
                    "colset allstates=allenums;\n" +
                    "colset integer=int timed;\n" +
                    "colset Receiver_PId=int;\n" +
                    "colset pair=product integer * integer timed;\n" +
                    "colset PIdType=integer;\n" +
                    "colset Boolean=bool;\n" +
                    "colset Natural=integer;");
            parse("colset ab =record a: int * b: int");

            //parse("val v = 1`1 ++ 1`2");


            //Parser.getParsedExpression("(false, 1)");


            /*
            parse("fun DeleteListElem (NIL, _) = NIL"+
                    "| DeleteListElem (head::tail, n) = if head=n THEN  tail  else  head::DeleteListElem (tail, n)");
            // */
        } catch (Throwable t) {
            t.printStackTrace(System.out);
        }
    }

    static void parse(String s) throws IdentifierCollision, SyntaxError, Warning, TypeError {
        String ds[] = s.split("\\;");
        for (String d : ds) {
            Parser.parseDeclaration(d);
        }
    }
}

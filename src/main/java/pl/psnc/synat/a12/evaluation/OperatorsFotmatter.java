package pl.psnc.synat.a12.evaluation;

public interface OperatorsFotmatter {

    StringBuilder getOperators();


    OperatorsFotmatter deletion(char c);


    OperatorsFotmatter insertion(char c);


    OperatorsFotmatter substitution(char old, char c);


    OperatorsFotmatter same(char c);

}
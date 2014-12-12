
enum BoolOp 
{
    equals,
    bigger,
    samller,
    biggerEquals,
    smallerEquals,
    differ,
    None,
};

enum BinaryOp
{
    plus,
    minus,
    mult,
    devide,
    None,
};

enum CmdType
{
    Assignment,
    BoolOpertion,
    Goto,
    Print,
    None
};

enum ExpType
{
    Variable,
    Number,
    BinaryOperation,
    None
};

class ErrorTupple
{
    public final int Code;
    public final String Message;
    public final int LineNumber;

    public ErrorTupple(int lineNumber, int code, String message) 
    {
        this.Code = code;
        this.LineNumber = lineNumber;
        this.Message = message;
    }
};
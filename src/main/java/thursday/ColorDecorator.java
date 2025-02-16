package thursday;

public class ColorDecorator implements ITextDecorator {
    private String color;

    public ColorDecorator (String color) {
        this.color = color;
    }

    @Override
    public String decorate(String text) {
        return color + text + "\u001B[0m";
    }

    //"\u001B is the Escape character (ESC), which is used to start an ANSI escape sequence."
    //"[0m is the reset code in ANSI escape sequences, which tells the terminal to reset any color formatting back to the default settings."
}

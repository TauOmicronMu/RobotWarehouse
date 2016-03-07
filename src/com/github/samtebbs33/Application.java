package com.github.samtebbs33;

import java.io.PrintStream;
import java.util.Scanner;

/**
 * Created by samtebbs on 01/02/2016.
 */
public class Application {

    protected PrintStream out, err;
    protected Scanner in;

    public Application(String[] args) {
        this.out = System.out;
        this.in = new Scanner(System.in);
    }

    public static void main(String[] args) {
        new Application(args);
    }

    protected void exit(int status) {
        System.exit(status);
    }

    public void print(Object obj) {
        out.print(obj);
    }

    public void println(Object obj) {
        out.println(obj);
    }

    public int inInt() {
        return in.nextInt();
    }

    public long inLong() {
        return in.nextLong();
    }

    public short inShort() {
        return in.nextShort();
    }

    public String inLine() {
        return in.nextLine();
    }

    public String inStr() {
        return in.next();
    }

    public byte inByte() {
        return in.nextByte();
    }

    public boolean hasInt() {
        return in.hasNextInt();
    }

    public boolean hasLong() {
        return in.hasNextLong();
    }

    public boolean hasShort() {
        return in.hasNextShort();
    }

    public boolean hasLine() {
        return in.hasNextLine();
    }

    public boolean hasStr() {
        return in.hasNext();
    }

    public boolean hasByte() {
        return in.hasNextByte();
    }

}

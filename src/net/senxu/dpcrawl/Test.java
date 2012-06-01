/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.senxu.dpcrawl;

/**
 *
 * @author sxu
 */
public class Test {
    
    public static void main(String[] args){
        String s="<li><a href=\"/shop/4174680#ur\" onclick=\"pageTracker._trackPageview('dp_search_readreview_beijing')\" class=\"B\" rel=\"nofollow\" ";
        String regEx="a href=\"/shop/";
        System.out.println(DPCrawl.ParseString(s, regEx));
    }
    
}

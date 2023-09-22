package com.regroup.rsunny.utils;

import org.springframework.stereotype.Service;

@Service
public class PagingUtil {

    public static String paging(int totalRows, int currPage, int rows) {
    	return paging(totalRows, currPage, rows, "movePage");
    }

    public static String paging(int totalRows, int currPage, int rows, String callback) {
    	StringBuffer sb = new StringBuffer();
    	
    	if(totalRows==0) return "<ul><li>1</li></ul>";

    	int totalPages = (int)Math.floor((totalRows + rows - 1) / rows);
    	if(totalPages==0) totalPages = 1;
    	int pages = 10;

    	int groupSeed = (int)Math.floor( (currPage - 1) / pages );
    	int lastSeed = (int)Math.floor( (totalPages - 1) / pages );
    	int frPage = groupSeed * pages + 1;
    	int toPage = (groupSeed+1) * pages;
    	if(toPage > totalPages) toPage = totalPages;

    	int goPage = 0;
    	
    	//sb.append("<ul>");
    	if(groupSeed > 0) {
    		goPage = (groupSeed - 1) * pages + 1;
    		sb.append(String.format("<li class='prev'><a href='javascript:%s(%d);'>이전</a></li>", callback, goPage));
    	}
    	
    	for(int i = frPage; i <= toPage; i++) {
    		if(i==currPage) {
    			sb.append(String.format("<li class='num on'><a href='javascript:%s(%d);'>%d</a></li>", callback, i, i));
    		}
    		else {
    			sb.append(String.format("<li class='num'><a href='javascript:%s(%d);'>%d</a></li>", callback, i, i));
    		}
    	}

    	if(groupSeed < lastSeed) {
    		goPage = (groupSeed + 1) * pages + 1;
    		sb.append(String.format("<li class='next'><a href='javascript:%s(%d);'>다음</a></li>", callback, goPage));
    	}
    	//sb.append("</ul>");

    	return sb.toString();
    }

    public static String pagingAdmin(int totalRows, int currPage, int rows, String callback) {
    	StringBuffer sb = new StringBuffer();
    	
    	if(totalRows==0) return "<ul><li>1</li></ul>";

    	int totalPages = (int)Math.floor((totalRows + rows - 1) / rows);
    	if(totalPages==0) totalPages = 1;
    	int pages = 10;

    	int groupSeed = (int)Math.floor( (currPage - 1) / pages );
    	int lastSeed = (int)Math.floor( (totalPages - 1) / pages );
    	int frPage = groupSeed * pages + 1;
    	int toPage = (groupSeed+1) * pages;
    	if(toPage > totalPages) toPage = totalPages;

    	int goPage = 0;
    	
    	sb.append("<ul>");
    	if(groupSeed > 0) {
    		sb.append(String.format("<li class='first'><a href='javascript:%s(%d);'>처음</a></li>", callback, 1));
    		goPage = (groupSeed - 1) * pages + 1;
    		sb.append(String.format("<li class='prev'><a href='javascript:%s(%d);'>이전</a></li>", callback, goPage));
    	}
    	
    	for(int i = frPage; i <= toPage; i++) {
    		if(i==currPage) {
    			sb.append(String.format("<li class='num on'><a href='javascript:%s(%d);'>%d</a></li>", callback, i, i));
    		}
    		else {
    			sb.append(String.format("<li class='num'><a href='javascript:%s(%d);'>%d</a></li>", callback, i, i));
    		}
    	}

    	if(groupSeed < lastSeed) {
    		goPage = (groupSeed + 1) * pages + 1;
    		sb.append(String.format("<li class='next'><a href='javascript:%s(%d);'>다음</a></li>", callback, goPage));
    		sb.append(String.format("<li class='end'><a href='javascript:%s(%d);'>마지막</a></li>", callback, totalPages));
    	}
    	sb.append("</ul>");

    	return sb.toString();
    }

}

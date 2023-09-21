package com.Accio;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet("/Search")
public class Search extends HttpServlet {

    //when Search servlet get trigger request and response object are created and doGet method will be called automatically
protected void  doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
//getting keyword from frontend
    String keyword=request.getParameter("keyword");
    //setting up connection to database
    Connection connection=DatabaseConnection.getConnection();
    try {
        //inserting keyword and link in history tabale in database for implementing history function

       PreparedStatement preparedStatement= connection.prepareStatement("insert into history values(?,?);");
        preparedStatement.setString(1,keyword);
        preparedStatement.setString(2,"http://localhost:8080/Search_Engine/Search?keyword="+keyword);
        preparedStatement.executeUpdate();

        //getting result after running ranking query and stroring all results to Resulset
        ResultSet resultSet = connection.createStatement().executeQuery("select *, (length(lower(pagetext))-length(replace(lower(pagetext),'" + keyword.toLowerCase() + "','')))/ length('" + keyword.toLowerCase() + "') as count from pages order by count desc limit 30 ;");

        ArrayList<SearchResult> results = new ArrayList<>();
//transforming values from resultset to results arrayList
        while (resultSet.next()) {
            SearchResult searchResult = new SearchResult();
            searchResult.setTitle(resultSet.getString("pageTitle"));
            searchResult.setLink(resultSet.getString("pageLink"));
            results.add(searchResult);
        }
//dipslaing resul array list to consol
        for(SearchResult result: results){
            System.out.println(result.title+" "+result.link);
        }

        //setting attribute with name "results" to request object and that attribute value to results i.e Arraylist
        request.setAttribute("results",results);
        //getting request dispatcher and frowarding request object to search.jsp file for rendering with assigned attribute to use in that file
        request.getRequestDispatcher("search.jsp").forward(request,response);

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
    }
    catch (SQLException | ServletException sqlException){
        sqlException.printStackTrace();
    }

}
}

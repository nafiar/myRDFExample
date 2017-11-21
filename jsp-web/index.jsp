
<!DOCTYPE html>
<html>

<head>
  	<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">
</head>	
<body>
	<div class="container">
		<form action = "reasoning" method = "POST">
			<input type="text" name="nameSearch" placeholder="nama yang ingin di cari"/>
			<button type="submit" class="btn btn-primary"> Submit </button>
		</form>
	</div >
        <table class="table table-striped table-hover table-bordered">
            <thead>
                <tr>
                    <th>#</th>
                    <th>Subject</th>
                    <th>Predicate</th>
                    <th>Object</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <%@ page import="java.util.List" %>
                    <%	
                        if(request.getAttribute("results") != null) {

                            int count = 0;
                            for(List<String> statement : results) {
                                String subject = statement.get(0);
                                String predicate = statement.get(1);
                                String object = statement.get(2);
                                out.println("<td>" + count+1 + "</td>");
                                out.println("<td>" + subject + "</td>");
                                out.println("<td>" + pedicate + "</td>");
                                out.println("<td>" + object + "</td>");
                            }
                        }
                     //    String[] triples =  request.getParameter("triples");
                    	// if(triples.length > 0)
                    	// {
                    	// 	int i;
                    	// 	for(i=0;i<triples.legth;i++)
                    	// 	{
                    	// 		out.print("isi triples : "+triples[i]+"<br>");
                    	// 	}
                    	// }
                    	// else
                    	// {
                    	// 	out.print("triples kosong<br>");
                    	// }
                     %> 
                </tr>
            </tbody>
        </table>
	<script type="text/javascript" src="js/bootstrap.min.js"></script>
</body>
</html>
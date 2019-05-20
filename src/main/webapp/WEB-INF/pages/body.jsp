<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page pageEncoding="UTF-8" language="java" %>

<body>
<div style="width:800px; margin: 0 auto">
    <c:forEach items="${table}" var="t">
        <h4>${t.title}</h4> <%--这个是类的说明--%>
        <h5>${t.tag}</h5>   <%--这个是每个请求的说明，方便生成文档后进行整理--%>
        <table border="1" cellspacing="0" cellpadding="0" width="100%">
            <tr class="bg">
                <td colspan="5"><c:out value="${t.tag}"/></td>
            </tr>
            <tr>
                <td>接口描述</td>
                <td colspan="4">${t.description}</td>
            </tr>
            <tr>
                <td>URL</td>
                <td colspan="4">${t.url}</td>
            </tr>
            <tr>
                <td>请求方式</td>
                <td colspan="4">${t.requestType}</td>
            </tr>
            <tr>
                <td>请求类型</td>
                <td colspan="4">${t.requestForm}</td>
            </tr>
            <tr>
                <td>返回类型</td>
                <td colspan="4">${t.responseForm}</td>
            </tr>

            <tr class="bg" align="center">
                <td>参数名</td>
                <td>数据类型</td>
                <td>是否必填</td>
                <td>说明</td>
            </tr>
            <c:forEach items="${t.requestList}" var="req">
                <tr align="center">
                    <td>${req.name}</td>
                        <%--数据类型  如果是普通--%>
                    <c:if test="${req.IBean ==null}" var="flag" scope="session">
                        <td>${req.type}</td>
                    </c:if>
                        <%--数据类型是true,则需要做超链接--%>
                    <c:if test="${not flag}">
                        <td><a href="#_${req.type}">${req.type}</a></td>
                    </c:if>
                    <td>
                        <c:choose>
                            <c:when test="${req.require == true}">Y</c:when>
                            <c:otherwise>N</c:otherwise>
                        </c:choose>
                    </td>
                    <td>${req.remark}</td>
                </tr>
            </c:forEach>

            <tr class="bg" align="center">
                <td>状态码</td>
                <td>描述</td>
                <td colspan="3">说明</td>
            </tr>
            <c:forEach items="${t.responseList}" var="res">
                <tr align="center">
                    <td>${res.name}</td>
                    <td>${res.description}</td>
                    <td colspan="3">${res.remark}</td>
                </tr>
            </c:forEach>

            <c:forEach items="${t.requestBeans}" var="reqBean">
                <td id="_${reqBean.name}">${reqBean.name}</td>
                <tr class="bg" align="center">
                    <td>参数名</td>
                    <td>描述</td>
                    <td>参数类型</td>
                    <td>是否必填</td>
                </tr>
                <c:forEach items="${reqBean.beanProps}" var="b">
                    <tr align="center">
                        <td>${b.name}</td>
                        <td>${b.description}</td>
                        <td>${b.type}</td>
                        <td>
                            <c:choose>
                                <c:when test="${b.required == true}">Y</c:when>
                                <c:otherwise>N</c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </c:forEach>
        </table>
        <br>
    </c:forEach>
</div>
</body>
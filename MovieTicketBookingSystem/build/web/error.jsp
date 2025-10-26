<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page isErrorPage="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Lỗi - MovieNow</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
        }

        .error-container {
            text-align: center;
            max-width: 600px;
            padding: 40px;
            background: rgba(255, 255, 255, 0.1);
            border-radius: 20px;
            backdrop-filter: blur(10px);
            box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);
        }

        .error-icon {
            font-size: 120px;
            margin-bottom: 20px;
            opacity: 0.8;
        }

        .error-code {
            font-size: 72px;
            font-weight: 700;
            margin-bottom: 10px;
            text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);
        }

        .error-message {
            font-size: 24px;
            margin-bottom: 20px;
            font-weight: 300;
        }

        .error-description {
            font-size: 16px;
            margin-bottom: 30px;
            opacity: 0.9;
            line-height: 1.6;
        }

        .btn-home {
            display: inline-block;
            padding: 15px 30px;
            background: linear-gradient(135deg, #e50914, #c90812);
            color: white;
            text-decoration: none;
            border-radius: 50px;
            font-weight: 600;
            font-size: 16px;
            transition: all 0.3s ease;
            box-shadow: 0 4px 15px rgba(229, 9, 20, 0.4);
        }

        .btn-home:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(229, 9, 20, 0.6);
        }

        .error-details {
            margin-top: 30px;
            padding: 20px;
            background: rgba(0, 0, 0, 0.2);
            border-radius: 10px;
            font-family: 'Courier New', monospace;
            font-size: 14px;
            text-align: left;
            max-height: 200px;
            overflow-y: auto;
        }

        @media (max-width: 768px) {
            .error-container {
                margin: 20px;
                padding: 30px 20px;
            }
            
            .error-code {
                font-size: 48px;
            }
            
            .error-message {
                font-size: 20px;
            }
        }
    </style>
</head>
<body>
    <div class="error-container">
        <div class="error-icon">
            <i class="fas fa-exclamation-triangle"></i>
        </div>
        
        <div class="error-code">
            <%= response.getStatus() %>
        </div>
        
        <div class="error-message">
            <c:choose>
                <c:when test="${response.status == 404}">
                    Trang không tìm thấy
                </c:when>
                <c:when test="${response.status == 500}">
                    Lỗi máy chủ
                </c:when>
                <c:otherwise>
                    Đã xảy ra lỗi
                </c:otherwise>
            </c:choose>
        </div>
        
        <div class="error-description">
            <c:choose>
                <c:when test="${response.status == 404}">
                    Xin lỗi, trang bạn đang tìm kiếm không tồn tại hoặc đã bị di chuyển.
                </c:when>
                <c:when test="${response.status == 500}">
                    Có lỗi xảy ra trên máy chủ. Vui lòng thử lại sau hoặc liên hệ quản trị viên.
                </c:when>
                <c:otherwise>
                    Đã xảy ra lỗi không mong muốn. Vui lòng thử lại sau.
                </c:otherwise>
            </c:choose>
        </div>
        
        <a href="home" class="btn-home">
            <i class="fas fa-home"></i>
            Về trang chủ
        </a>
        
        <c:if test="${not empty exception}">
            <div class="error-details">
                <strong>Chi tiết lỗi:</strong><br>
                <%= exception.getMessage() %>
            </div>
        </c:if>
    </div>
</body>
</html>

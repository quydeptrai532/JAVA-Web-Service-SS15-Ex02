Phần 1 - Phân tích logic
1. Sự khác biệt cơ bản về CSRF giữa Web truyền thống và REST API:

Ứng dụng Web truyền thống (Session-based): Sử dụng Cookie để lưu trữ Session ID (ví dụ: JSESSIONID). Khi người dùng đăng nhập thành công, trình duyệt sẽ tự động đính kèm Cookie này vào mọi request gửi đến server đó. Lỗ hổng CSRF (Cross-Site Request Forgery) lợi dụng cơ chế "tự động đính kèm" này. Kẻ tấn công có thể lừa người dùng nhấp vào một liên kết độc hại, khiến trình duyệt gửi request (ví dụ: chuyển tiền, đổi mật khẩu) lên server. Server thấy có Session Cookie hợp lệ nên vẫn xử lý request đó mà không biết nó đến từ một nguồn bị giả mạo. Để chống lại, ta cần một CSRF Token riêng biệt sinh ra trong form HTML.

Ứng dụng REST API (Stateless/Token-based cho Mobile): Khi làm việc với các ứng dụng di động, hệ thống thường sử dụng Token (như JWT) để xác thực. Client lưu trữ token này (ví dụ: trong SharedPreferences của Android) và phải tự thêm nó vào Header của mỗi request (thường là Authorization: Bearer <token>). Trình duyệt/Client di động không tự động gửi header này khi người dùng bị lừa bấm vào link độc hại. Do đó, nếu API của bạn hoàn toàn phi trạng thái (stateless) và không dựa vào Cookie để xác thực, nguy cơ bị tấn công CSRF gần như không tồn tại.

2. Tại sao vô hiệu hóa CSRF mù quáng lại nguy hiểm?

Nếu bạn đang xây dựng một ứng dụng web truyền thống (render HTML từ server như Thymeleaf, JSP) hoặc API có sử dụng Cookie để lưu thông tin xác thực, việc gọi .csrf().disable() sẽ mở toang cánh cửa cho hacker. Kẻ xấu có thể nhúng các đoạn mã script ẩn hoặc form giả mạo trên các trang web khác để ép trình duyệt của nạn nhân thực hiện các thao tác thay đổi dữ liệu (POST, PUT, DELETE) trên hệ thống của bạn mà nạn nhân không hề hay biết.
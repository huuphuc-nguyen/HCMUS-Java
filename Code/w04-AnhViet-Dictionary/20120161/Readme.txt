Họ tên: Nguyễn Hữu Phúc
MSSV: 20120161
Email: 20120161@student.hcmus.edu.vn

I. Cách chạy ứng dụng
	B1: Vào thư mục Release.
	B2: Chạy file DictionaryApp.jar.
	Lưu ý: Thư mục files là cần thiết để có thể chạy ứng dụng này, phải đặt cùng cấp với file .jar.

II. Cách xem code
	B1: Vào thư mục Source.
	B2: Vào thư mục Code, tại đây có các file chứa toàn bộ code của chương trình.

III. Cách xem cả project
	- Project được hoàn thành với VSCode. Có một thư mục lưu cả cấu trúc project theo VSCode. 
	- Cách mở thư mục đó:
		B1: Sử dụng VSCode để mở thư mục.
		B2: Vào thư mục Source.
		B3: Mở thư mục VSCode_Project.
IV. Phụ chú:
	- Có sự hỗ trợ của chatGPT.
	- Sử dụng 1 thư viện bên ngoài để đọc file XML.
	- Sử dụng HashMap để lưu các cặp từ và nghĩa.
	- Sử dụng JSpinner để chọn ngày vì chưa tìm được thư viện JDataPicker phù hợp với VSCode.
	- Link Demo: https://youtu.be/wBg9a0vqPwQ

V. Các tính năng đã làm:
	- Search: search theo thay đổi của từng ký tự nhập/xóa vào.

	- Thêm từ: ngoài thêm từ như bình thường, nếu từ đã tồn tại, chương trình sẽ xác nhận ý kiến người dùng,
		nếu người dùng đồng ý thêm từ đã tồn tại, định nghĩa mới sẽ được thay thế cho định nghĩa cũ, sau khi thêm sẽ thông báo thêm thành công.

	- Xóa từ: cho phép người dùng xóa hàng loạt từ, trước khi xóa sẽ xác nhận lại người dùng, sau khi xóa sẽ thông báo xóa thành công.

	- Thêm vào danh sách yêu thích: cho phép người dùng thêm hàng loạt từ vào danh sách, sau khi thêm sẽ có thông báo thêm thành công.

	- Lưu lịch sử: thông kê lịch sử như yêu cầu, có thêm tính năng "clear history" cho phép người dùng chủ động quản lý dung lượng của chương trình.	
			tránh trường hợp lịch sử quá dài, chiếm nhiều bộ nhớ máy tính.

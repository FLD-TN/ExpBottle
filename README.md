# ExpBottle Plugin

Plugin Minecraft 1.21.1 cho phép player rút kinh nghiệm thành bottle để lưu trữ và sử dụng sau.

## Tính năng

- **Rút kinh nghiệm**: Sử dụng lệnh `/exp <amount|all>` để rút kinh nghiệm thành bottle
- **Bảo mật**: Mỗi bottle có mã secret duy nhất để tránh dupe
- **Sử dụng linh hoạt**: 
  - Click phải vào bottle để sử dụng 1000 exp
  - Shift + Click phải để sử dụng tất cả exp trong bottle
- **Giao diện thân thiện**: Bottle có tên và lore hiển thị thông tin chi tiết

## Cách sử dụng

### Lệnh

- `/exp <số lượng>` - Rút một lượng exp cụ thể thành bottle
- `/exp all` - Rút tất cả exp hiện tại thành bottle

### Ví dụ

```
/exp 1000000    # Rút 1 triệu exp thành bottle
/exp all        # Rút tất cả exp thành bottle
```

### Sử dụng bottle

1. **Sử dụng từng phần**: Click phải vào bottle để sử dụng 1000 exp mỗi lần
2. **Sử dụng tất cả**: Giữ Shift + Click phải để sử dụng hết exp trong bottle

## Quyền hạn

- `expbottle.use` - Cho phép sử dụng lệnh exp (mặc định: true)
- `expbottle.admin` - Quyền admin (mặc định: op)

## Cài đặt

1. Build plugin bằng Maven: `mvn clean install`
2. Copy file `.jar` từ thư mục `target/` vào thư mục `plugins/` của server
3. Restart server
4. Cấu hình trong file `config.yml` nếu cần

## Cấu hình

File `config.yml` cho phép tùy chỉnh:
- Tin nhắn hiển thị
- Tên và mô tả của bottle
- Định dạng hiển thị

## Bảo mật

- Mỗi bottle có mã secret SHA-256 duy nhất
- Kiểm tra tính toàn vẹn khi sử dụng
- Ngăn chặn dupe và hack

## Yêu cầu

- Minecraft 1.21.1
- Paper/Spigot server
- Java 21

## Build

```bash
mvn clean install
```

File plugin sẽ được tạo trong `target/ExpBottle-1.0.0.jar`
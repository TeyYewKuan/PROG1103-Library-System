public class Librarian extends User {

    public Librarian(String userId, String password, String name) {
        super(userId, password, name, "Librarian");
    }

    @Override
    public String getRoleWelcomeMessage() {
        return "管理员登录成功！欢迎，" + getName() + " 老师。你已解锁图书录入与权限管理功能。";
    }
}

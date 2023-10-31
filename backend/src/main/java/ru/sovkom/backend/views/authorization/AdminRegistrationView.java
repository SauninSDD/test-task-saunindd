package ru.sovkom.backend.views.authorization;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import ru.sovkom.backend.entities.Admin;
import ru.sovkom.backend.services.AdminService;
import com.vaadin.flow.data.binder.Binder;

@Route("register")
@PageTitle("Register")
@AnonymousAllowed
public class AdminRegistrationView extends VerticalLayout {

    private final AdminService adminService;
    private final Admin admin;
    private final Binder<Admin> binder;

    private final EmailField email = new EmailField("Email");
    private final PasswordField password = new PasswordField("Пароль");
    private final PasswordField confirmPassword = new PasswordField("Повторите пароль");

    private final Button registerButton = new Button("Зарегистрироваться");

    private final Button loginButton = new Button("Вернуться к авторизации");


    public AdminRegistrationView(AdminService adminService) {
        this.adminService = adminService;
        this.admin = new Admin();
        this.binder = new Binder<>(Admin.class);

        setClassName("login-view");
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
        setSizeFull();

        H1 title = new H1("Регистрация админа");
        add(title);

        FormLayout form = new FormLayout();
        form.add(email, password, confirmPassword);
        form.getStyle().set("width", "300px");
        form.getStyle().set("margin", "0 auto");

        binder.bindInstanceFields(this);

        registerButton.addClickListener(event -> registerAdmin());

        loginButton.addClickListener(event -> getUI().ifPresent(ui -> ui.navigate("login")));

        add(form, registerButton, loginButton);
    }


    private void registerAdmin() {
        if (binder.writeBeanIfValid(admin)) {
            if (password.getValue().equals(confirmPassword.getValue())) {
                adminService.signUp(admin);
                Notification.show("Успешная регистрация!");
                getUI().ifPresent(ui -> ui.navigate("login"));
            } else {
                Notification.show("Пароли не совпадают.");
            }
        } else {
            Notification.show("Пожалуйста, введите корректные данные :)");
        }
    }
}

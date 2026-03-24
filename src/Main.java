import java.lang.annotation.*;
import java.lang.reflect.Method;
import java.util.function.Predicate;

// ===== 1.1 Printable =====
@FunctionalInterface
interface Printable {
    void print();
}

// ===== 2.1 Аннотация =====
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@interface DeprecatedEx {
    String message();
}

// ===== Пример класса =====
@DeprecatedEx(message = "Используй NewClass")
class OldClass {

    @DeprecatedEx(message = "Используй newMethod()")
    public void oldMethod() {
        System.out.println("Старый метод");
    }
}

// ===== Обработчик =====
class AnnotationHandler {

    public static void checkClass(Class<?> clazz) {

        if (clazz.isAnnotationPresent(DeprecatedEx.class)) {
            DeprecatedEx ann = clazz.getAnnotation(DeprecatedEx.class);
            System.out.println("! класс '" + clazz.getSimpleName() +
                    "' устарел – альтернатива: '" + ann.message() + "'");
        }

        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(DeprecatedEx.class)) {
                DeprecatedEx ann = method.getAnnotation(DeprecatedEx.class);
                System.out.println("! метод '" + method.getName() +
                        "' устарел – альтернатива: '" + ann.message() + "'");
            }
        }
    }
}

// ===== MAIN =====
public class Main {
    public static void main(String[] args) {

        System.out.println("=== 1.1 Printable ===");
        Printable printable = () -> System.out.println("Печать через лямбду");
        printable.print();

        System.out.println("\n=== 1.2 Predicate (null + пусто) ===");
        Predicate<String> notNull = s -> s != null;
        Predicate<String> notEmpty = s -> !s.isEmpty();
        Predicate<String> valid = notNull.and(notEmpty);

        String test = "Hello";
        System.out.println("Строка валидна: " + valid.test(test));

        System.out.println("\n=== 1.3 Проверка строки ===");
        Predicate<String> check = s ->
                (s.startsWith("J") || s.startsWith("N")) && s.endsWith("A");

        System.out.println("JAVA -> " + check.test("JAVA"));
        System.out.println("NINA -> " + check.test("NINA"));
        System.out.println("KIRA -> " + check.test("KIRA"));

        System.out.println("\n=== 2.1 Аннотации ===");
        AnnotationHandler.checkClass(OldClass.class);
    }
}
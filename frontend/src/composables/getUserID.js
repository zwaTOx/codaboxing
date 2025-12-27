import { jwtDecode } from "jwt-decode";

export const getUserID = () => {
    const token = localStorage.getItem('token');

    if (token) {
        try {
            const decoded = jwtDecode(token);
            console.log(decoded);
            
            // Пример доступа к данным:
            const userId = decoded.user_id; // Или decoded.id, зависит от вашего бэкенда
            const expirationDate = new Date(decoded.exp * 1000);
            
            console.log("ID пользователя:", userId);
            console.log("Токен истекает:", expirationDate);

            return userId;
        } catch (error) {
            console.error("Ошибка декодирования токена:", error);
        }
    }
}

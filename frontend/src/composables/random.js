export const getRandomNumber = (min, max) => {
    return Math.round(Math.random() * (max - min) + min);
};
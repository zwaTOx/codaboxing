export const getInitials = (value) => {
    return value.split(' ').map(name => name[0]).join('');
};
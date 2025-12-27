export const dictToVariables = (dict) => {
    if (dict === null || dict === undefined)
        return "";
    let variables = "";
    let count = 0;
    let length = Object.entries(dict).length;
    Object.entries(dict).forEach(([key, value]) => {
        count++;
        variables += `${key} = ${value}`
        if (count < length)
            variables += ", ";
    });

    return variables;
};
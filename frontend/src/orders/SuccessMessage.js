import React from "react";

export function SuccessMessage({ success }) {
    if (success) return <p style={{color: "green"}}> ¡Guardado! </p>
}
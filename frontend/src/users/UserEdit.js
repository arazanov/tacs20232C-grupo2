import {useEffect, useState} from "react";
import {UserForm} from "./UserForm";
import {useNavigate} from "react-router-dom";

export default function UserEdit() {
    let token = localStorage.getItem('token');
    const navigate = useNavigate();
    const [user, setUser] = useState({
        username: '',
        email: '',
        password: ''
    });

    const request = () => {
        return {
            method: 'PUT',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token
            },
            body: JSON.stringify(user)
        };
    };

    useEffect(() => {
        fetch("/user", {
            method: 'GET',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token
            }
        })
            .then(response => {
                if(response.status === 401) throw new Error("No autorizado");
                return response.json();
            })
            .then(setUser)
            .catch(e => {
                alert(e);
                navigate("/");
            });
    }, [token, navigate]);

    return <UserForm request={request} nav={-1} enableDelete={true} title={'Editar perfil'}
                     user={user} setUser={setUser}/>;

}
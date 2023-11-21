import {useEffect, useState} from "react";
import {UserForm} from "./UserForm";
import {useNavigate} from "react-router-dom";
import {Button} from "reactstrap";
import {useAuth} from "../AuthContext";

export default function UserEdit() {
    let {token, setToken} = useAuth();
    const navigate = useNavigate();
    const [user, setUser] = useState({
        username: '',
        email: '',
        password: ''
    });

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
                if (response.status === 401)
                    throw new Error("No autorizado");
                return response.json();
            })
            .then(setUser)
            .catch(e => {
                alert(e);
                navigate("/");
            });
    }, [token, navigate]);

    const handleSubmit = () => fetch('/users', {
        method: 'PUT',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
        },
        body: JSON.stringify(user)
    })
        .then(response => {
            if (response.status === 409) {
                throw new Error("Invalid username");
            }
            return response.json();
        })
        .then(data => {
            setToken(data.token);
            navigate("/orders");
        });

    function remove() {
        fetch('/users', {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token
            }
        }).then(() => navigate('/'));
    }

    return <UserForm title={'Editar perfil'} user={user} setUser={setUser} handleSubmit={handleSubmit}>
        <Button color="danger" onClick={() => remove()}>Eliminar</Button>
    </UserForm>;

}
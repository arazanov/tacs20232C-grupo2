import {useEffect, useState} from 'react';
import {Button, Container, Form, FormGroup, Input, Label} from 'reactstrap';
import AppNavbar from '../navbar/AppNavbar';
import {Link, useNavigate} from "react-router-dom";
import {useAuth} from "../AuthContext";

export default function Login() {
    const [user, setUser] = useState({
        username: "",
        password: ""
    });
    const [badCredentials, setBadCredentials] = useState(false);
    const navigate = useNavigate();
    const { setToken } = useAuth();

    useEffect(() => {
        setToken(null);
    }, [setToken]);

    function setUsername(e) {
        setUser({ ...user, username: e.target.value });
    }

    function setPassword(e) {
        setUser({ ...user, password: e.target.value });
    }

    function handleSubmit(e) {
        e.preventDefault();
        fetch('/', {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(user),
        })
            .then(response => {
                if (response.status === 401)
                    throw new Error(response.statusText);
                return response.json();
            })
            .then(data => {
                setBadCredentials(false);
                setToken(data.token);
                navigate("/orders");
            })
            .catch(e => {
                console.log(e);
                setBadCredentials(true);
            });
    }

    function BadCredentialsMessage({badCredentials}) {
        if (badCredentials)
            return <p style={{color: "red"}}>
                Nombre de usuario o contraseña incorrecto </p>;
    }

    return <div>
        <AppNavbar/>
        <Container>
            <h2 style={{paddingTop: 50, paddingBottom: 50}}>Iniciar sesión</h2>
            <Form onSubmit={handleSubmit}>
                <FormGroup>
                    <Label for="username">Nombre de usuario</Label>
                    <Input type="text" id="username" defaultValue={user.username} autoComplete={"on"}
                           onChange={setUsername}/>
                </FormGroup>
                <FormGroup>
                    <Label for="password">Constraseña</Label>
                    <Input type="password" id="password" defaultValue={user.password}
                           onChange={setPassword}/>
                </FormGroup>
                <BadCredentialsMessage badCredentials={badCredentials}/>
                <FormGroup style={{paddingTop: 50}}>
                    <Button color="primary" type="submit">Enviar</Button>{' '}
                </FormGroup>
            </Form>
            <Link to={"/signup"}>
                Registrarse
            </Link>
        </Container>
    </div>
}
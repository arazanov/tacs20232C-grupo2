import AppNavbar from "../navbar/AppNavbar";
import {Button, Container, Form, FormFeedback, FormGroup, Input, Label} from "reactstrap";
import {useNavigate} from "react-router-dom";
import {useState} from "react";

export function UserForm({ request, nav, title, user, setUser, setToken, children }) {
    const navigate = useNavigate();
    const [invalidPassword, setInvalidPassword] = useState(false);
    const [invalidUsername, setInvalidUsername] = useState(false);

    function setUsername(e) {
        setInvalidUsername(false);
        setUser({ ...user, username: e.target.value });
    }

    function setEmail(e) {
        setInvalidUsername(false);
        setUser({ ...user, email: e.target.value });
    }

    function setPassword(e) {
        setInvalidPassword(false);
        setUser({ ...user, password: e.target.value });
    }

    function handleSubmit(e) {
        e.preventDefault();

        if (user.password === '') {
            setInvalidPassword(true);
            return;
        }

        fetch('/users', request())
            .then(response => {
                if (response.status === 409) {
                    throw new Error(response.statusText);
                }
                return response.json();
            })
            .then(data => {
                setToken(data.token);
                navigate(nav);
            })
            .catch(e => {
                setInvalidUsername(true);
                console.log(e);
            });
    }

    return <div>
        <AppNavbar/>
        <Container>

            <h2 style={{paddingTop: 50, paddingBottom: 50}}>{title}</h2>

            <Form onSubmit={handleSubmit}>
                <FormGroup>
                    <Label for="username">Nombre de usuario</Label>
                    <Input type="text" id="username" defaultValue={user.username} autoComplete="off"
                           onChange={setUsername} invalid={invalidUsername}/>
                    <FormFeedback valid={!invalidUsername}>Ya existe el nombre de usuario o contraseña</FormFeedback>
                </FormGroup>

                <FormGroup>
                    <Label for="email">Email</Label>
                    <Input type="email" id="email" defaultValue={user.email} autoComplete="off"
                           onChange={setEmail} invalid={invalidUsername}/>
                    <FormFeedback valid={!invalidUsername}>Ya existe el nombre de usuario o contraseña</FormFeedback>
                </FormGroup>

                <FormGroup>
                    <Label for="password">Contraseña</Label>
                    <Input type="password" id="password" defaultValue={user.password} autoComplete="off"
                           onChange={setPassword} invalid={invalidPassword}/>
                    <FormFeedback valid={!invalidPassword}>Este campo es obligatorio</FormFeedback>
                </FormGroup>

                <FormGroup style={{paddingTop: 50}}>
                    <Button color="primary" type="submit">Guardar</Button>{' '}
                    <Button color="secondary" onClick={() => navigate(-1)}>Cancelar</Button>{' '}
                    {children}
                </FormGroup>
            </Form>

        </Container>
    </div>

}
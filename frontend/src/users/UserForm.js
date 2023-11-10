import AppNavbar from "../navbar/AppNavbar";
import {Button, Container, Form, FormFeedback, FormGroup, Input, Label} from "reactstrap";
import {useNavigate} from "react-router-dom";

export function UserForm({ handleSubmit, user, setUsername, setEmail, setPassword, title, invalid }) {
    const navigate = useNavigate();
    return <div>
        <AppNavbar/>
        <Container>
            <h2 style={{paddingTop: 50, paddingBottom: 50}}>{title}</h2>
            <Form onSubmit={handleSubmit}>
                <FormGroup>
                    <Label for="username">Nombre de usuario</Label>
                    <Input type="text" id="username" defaultValue={user.username} autoComplete="off"
                           onChange={e => setUsername(e.target.value)}/>
                </FormGroup>
                <FormGroup>
                    <Label for="email">Email</Label>
                    <Input type="email" id="email" defaultValue={user.email} autoComplete="off"
                           onChange={e => setEmail(e.target.value)}/>
                </FormGroup>
                <FormGroup>
                    <Label for="password">Contraseña</Label>
                    <Input type="password" id="password" defaultValue={user.password} autoComplete="off"
                           onChange={e => setPassword(e.target.value)}
                           invalid={invalid}/>
                    <FormFeedback valid={!invalid}>Este campo es obligatorio</FormFeedback>
                </FormGroup>
                <FormGroup style={{paddingTop: 50}}>
                    <Button color="primary" type="submit">Guardar</Button>{' '}
                    <Button color="secondary" onClick={() => navigate(-1)}>Cancelar</Button>
                </FormGroup>
            </Form>
        </Container>
    </div>;
}
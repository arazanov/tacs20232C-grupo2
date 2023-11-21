import {useEffect, useState} from "react";
import {useParams} from "react-router";
import AppNavbar from "../navbar/AppNavbar";
import {Button, Col, Container, Form, FormGroup, Input, Label} from "reactstrap";
import {useNavigate} from "react-router-dom";
import {useAuth} from "../AuthContext"

export default function ItemEdit() {
    const { id } = useParams();
    const [item, setItem] = useState({
        id: '',
        version: null,
        description: '',
        quantity: null,
        unit: ''
    });
    const navigate = useNavigate();
    const { token } = useAuth();

    useEffect(() => {
        fetch('/items/' + id, {
            method: 'GET',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token
            }
        })
            .then(response => response.json())
            .then(data => {
                setItem(data);
            });
    }, [id, token]);

    function handleSubmit(e) {
        e.preventDefault();
        fetch('/items/' + id, {
            method: 'PUT',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token
            },
            body: JSON.stringify(item)
        }).then(response => {
            if (!response.ok) throw new Error(response.statusText);
            navigate(-1);
        }).catch(e => {
            console.log(e);
            alert("Ítem desactualizado, recargar página.");
            window.location.reload();
        })
    }

    return <div>
        <AppNavbar/>
        <Container>
            <h2 style={{paddingTop: 50, paddingBottom: 50}}>Editar ítem</h2>
            <Form onSubmit={handleSubmit}>
                <FormGroup row>
                    <Label for="description" sm={2}>Descripción</Label>
                    <Col sm={10}>
                        <Input type="text" id="description" placeholder="Agregar descripción" defaultValue={item.description} autoComplete={"off"}
                               onChange={e => setItem({
                                   ...item, description: e.target.value
                               })}/>
                    </Col>
                </FormGroup>
                <FormGroup row>
                    <Label for="quantity" sm={2}>Cantidad</Label>
                    <Col sm={10}>
                        <Input type="number" id="quantity" defaultValue={item.quantity}
                               onChange={e => setItem({
                                   ...item, quantity: e.target.value
                               })}/>
                    </Col>
                </FormGroup>
                <FormGroup row>
                    <Label for="unit" sm={2}>Unidad</Label>
                    <Col sm={10}>
                        <Input type="text" id="unit" defaultValue={item.unit} autoComplete={"off"}
                               onChange={e => setItem({
                                   ...item, unit: e.target.value
                               })}/>
                    </Col>
                </FormGroup>
                <FormGroup style={{paddingTop: 50}}>
                    <Button color="primary" type="submit">Guardar</Button>{' '}
                    <Button color="secondary" onClick={() => navigate(-1)}>Cancelar</Button>
                </FormGroup>
            </Form>
        </Container>
    </div>

}
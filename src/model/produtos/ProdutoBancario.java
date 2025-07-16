
package model.produtos;

import java.io.Serializable;
import model.Cliente;
import util.TipoProduto;

public interface ProdutoBancario extends Serializable {
    long getCodigoUnico ();
    void setCodigoUnico (long codigo);
    
    Cliente getCliente();
    String getDescricao();
    TipoProduto getTipoProduto();
}

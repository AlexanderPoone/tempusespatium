import UIKit
import SwiftIcons

class EndgameViewController: UIViewController {

    @IBOutlet weak var mAnimation: UIImageView!
    
    @IBOutlet weak var mReturnBtn: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()

       mReturnBtn!.setIcon(prefixText: "", prefixTextColor: .white, icon: .fontAwesomeSolid(.signOutAlt), iconColor: .white, postfixText: NSLocalizedString("return_main_menu", comment: ""), postfixTextColor: .white, backgroundColor: UIColor(named: "danger")!, forState: .normal, textSize: nil, iconSize: nil)
        
    }

}
